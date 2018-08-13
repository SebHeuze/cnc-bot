package org.cnc.cncbot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.cnc.cncbot.config.DBContext;
import org.cnc.cncbot.dto.UserSession;
import org.cnc.cncbot.dto.serverinfos.ServerInfoResponse;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.map.dao.DAOConstants;
import org.cnc.cncbot.stats.dao.AccountDAO;
import org.cnc.cncbot.stats.dao.AllianceDAO;
import org.cnc.cncbot.stats.dao.BaseDAO;
import org.cnc.cncbot.stats.dao.BatchLogDAO;
import org.cnc.cncbot.stats.dao.PlayerDAO;
import org.cnc.cncbot.stats.dao.PoiDAO;
import org.cnc.cncbot.stats.dao.SettingsDAO;
import org.cnc.cncbot.stats.entities.Account;
import org.cnc.cncbot.stats.entities.Alliance;
import org.cnc.cncbot.stats.entities.Base;
import org.cnc.cncbot.stats.entities.BatchLog;
import org.cnc.cncbot.stats.entities.Player;
import org.cnc.cncbot.stats.entities.Poi;
import org.cnc.cncbot.stats.entities.Settings;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Map service
 * @author sheuze
 *
 */
@Service
@Setter
@Slf4j
public class StatsService {

	private final GameService gameService;

	private final AccountDAO accountDAO;
	private final BatchLogDAO batchLogDAO;
	private final SettingsDAO settingDAO;
	
	private final AllianceDAO allianceDAO;
	private final PlayerDAO playerDAO;
	private final BaseDAO baseDAO;
	private final PoiDAO poiDAO;
	private final org.cnc.cncbot.map.dao.PoiDAO poiDAOMap;
	
	/**
	 * Formatteur de date SQL.
	 */
	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

	@Autowired
	public StatsService(GameService gameService, AccountDAO accountDAO, BatchLogDAO batchLogDAO, SettingsDAO settingDAO,
			AllianceDAO allianceDAO, PlayerDAO playerDAO, BaseDAO baseDAO, PoiDAO poiDAO, org.cnc.cncbot.map.dao.PoiDAO poiDAOMap) {
		this.gameService = gameService;
		this.accountDAO = accountDAO;
		this.batchLogDAO = batchLogDAO;
		this.settingDAO = settingDAO;

		this.allianceDAO = allianceDAO;
		this.playerDAO = playerDAO;
		this.baseDAO = baseDAO;
		this.poiDAO = poiDAO;
		this.poiDAOMap = poiDAOMap;
	}

	/**
	 * Main method for Stats Batch
	 * @throws BatchException
	 */
	public void statsJob() throws BatchException {
		DBContext.setDatasource("cctastats");

		log.info("Launch of stats batch");

		List<Account> accountList = this.accountDAO.findAll();

		BatchLog batchLog = new BatchLog();
		batchLog.setDateDebut(new Date());
		batchLog.setNbWorlds(accountList.size());

		batchLog = this.batchLogDAO.save(batchLog);

		log.info("Accounts size {}", accountList.size());

		int nbFails = 0;
		String failList = "";
		for (Account account : accountList) {
			DBContext.setSchema(DAOConstants.SCHEMA_PREFIX + account.getWorldId()); 
			Settings updateDate = this.settingDAO.getOne("date_last_update");

			DateTimeZone zone = DateTimeZone.forID(account.getTimezone());
			DateTime dt = new DateTime(zone);
			String currentDateTimezone = formatter.print(dt);

			if (!currentDateTimezone.equals(updateDate.getValue())){
				log.info("Launch stats for account {} on world {}", account.getUser(), account.getWorldId());
				try {
					this.statsJobForWorld(account, false);
				} catch (BatchException e){
					log.error("Error, end of stats batch for World {}", account.getWorldId(), e);
					nbFails++;
					failList += account.getWorldId() + ",";
				}
			} else {
				log.debug("Monde {} à jour",  account.getWorldId());

				Settings forceStats = this.settingDAO.getOne("force_stats");

				if (Integer.parseInt(forceStats.getValue()) == 1){
					log.info("Force stats for account {} on world {}", account.getUser(), account.getWorldId());
					try {
						this.statsJobForWorld(account, true);
					} catch (BatchException e){
						log.error("Error, end of stats batch for World {}", account.getWorldId(), e);
						nbFails++;
						failList += account.getWorldId() + ",";
					}
				}
			}
		}


		//On met à jour la durée du batch
		batchLog.setDateFin(new Date());
		batchLog.setFailList(failList);
		batchLog.setNbWorldsFails(nbFails);
		batchLog.setDuree(batchLog.getDateFin().getTime() - batchLog.getDateDebut().getTime());
		this.batchLogDAO.save(batchLog);

	}


	/**
	 * Main method for Stats Batch for a specific world
	 * @throws BatchException
	 * @param Account account to use
	 */
	public void statsJobForWorld(Account account, boolean statsOnly) throws BatchException {
		log.info("Start map batch of World : {}", account.getWorldId());
		
		UserSession userSession = new UserSession(account.getUser(), account.getPass(), account.getWorldId(), 0, 0,
				null, "World42Dummy", null);
		

		//@TODO call player endpoint to get username in launchWorld
		String gameSessionId = this.gameService.launchWorld(userSession);
		
		userSession.setGameSessionId(gameSessionId);

		ServerInfoResponse serverInfos = this.gameService.getServerInfos(userSession.getGameSessionId());
		
		
		if(!statsOnly){
		      //Deleting existing data
		      this.playerDAO.truncateTable();
		      this.baseDAO.truncateTable();
		      this.allianceDAO.truncateTable();
		      this.poiDAO.truncateTable();
		      
		      //Get players data

		      int maxRankingJoueur = Integer.valueOf(this.settingDAO.getOne("maxRankingJoueur").getValue());
		      List<Player> joueursListe = this.getJoueursData(maxRankingJoueur);
		      
		      // Removing duplicates
		      joueursListe = joueursListe.stream()
						      .distinct()
						      .collect(Collectors.toList());
		      
		      List<Base> basesList = this.extractBases(joueursListe);
		      
		      this.playerDAO.insertAll(joueursListe);
		      this.baseDAO.insertAll(basesList);
		      
		      //Récupération des alliances et POI
		      int maxRankingAlliance = Integer.valueOf(this.settingDAO.getOne("maxRankingAlliance").getValue());
		      List<Alliance> alliancesListe = this.getAlliancesData(maxRankingAlliance);
		      
		      // On enlève les dupliqués
		      alliancesListe = alliancesListe.stream()
				      .distinct()
				      .collect(Collectors.toList());
		      
		      List<Poi> poisList = this.extractPois(alliancesListe);

		      //We look for free POI in cncmap DB
			  DBContext.setDatasource("cncmap");
		      List<org.cnc.cncbot.stats.entities.Poi> allPOIList = this.poiDAOMap.findAll();
			  DBContext.setDatasource("cctastats");
				
		      poisList = poisList.stream()
				      .distinct()
				      .collect(Collectors.toList());
		      
		      //On ajoute sans alliance
		      alliancesListe.add(new Alliance(0, "", 0, 0, 9999, 0, 0, 0, 0, 0, 0,"", 0, new int[]{0,0,0,0,0,0,0,0}, new int[]{0,0,0,0,0,0,0,0}, new ArrayList<POI>()));
		      
		      this.allianceDAO.insertAll(compteActuel.getIdMonde(), alliancesListe);
		      this.poiDAO.insertAll(compteActuel.getIdMonde(), poisList);
		      
		      //On crée la date du jour pour la timezone concernée
		      DateTimeZone zone = DateTimeZone.forID(compteActuel.getTimezone());
		      DateTime dt = new DateTime(zone);
		      String currentDateTimezone = formatter.print(dt);
		      
		      //Archivage
		      this.playerDAO.archiver(currentDateTimezone);
		      this.baseDAO.archiver(currentDateTimezone);
		      this.allianceDAO.archiver(currentDateTimezone);
		      this.poiDAO.archiver(currentDateTimezone);
		      
		      //On crée une date en fonction de la timezone
		      this.settingDAO.updateSetting(userSession.getWorldId(), "date_last_update", currentDateTimezone);
		      this.scriptingDAO.updateCompteurs(userSession.getWorldId(), joueursListe.size(), alliancesListe.size());
		    }  
		    //Calcul stats
		    this.calculStats();
		    
		    //clearCache
		    this.cnCService.clearCache(userSession.getWorldId());
	}

}
