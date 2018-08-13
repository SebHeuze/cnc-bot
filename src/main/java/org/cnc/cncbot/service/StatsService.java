package org.cnc.cncbot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.cnc.cncbot.config.DBContext;
import org.cnc.cncbot.dto.PollWorld;
import org.cnc.cncbot.dto.S;
import org.cnc.cncbot.dto.UserSession;
import org.cnc.cncbot.dto.publicplayerinfo.C;
import org.cnc.cncbot.dto.publicplayerinfo.Ew;
import org.cnc.cncbot.dto.publicplayerinfo.PublicPlayerInfoResponse;
import org.cnc.cncbot.dto.rankingdata.P;
import org.cnc.cncbot.dto.rankingdata.RankingDataResponse;
import org.cnc.cncbot.dto.serverinfos.ServerInfoResponse;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.map.dao.DAOConstants;
import org.cnc.cncbot.map.dto.MapData;
import org.cnc.cncbot.map.entities.MapObject;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
	 * Intervalle de récupération pour les rangs.
	 */
	@Value("${cncbot.stats.ranking_interval}")
	private int rankingInterval; 


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
			List<Player> joueursListe = this.getPlayerData(maxRankingJoueur);

			// Removing duplicates
			joueursListe = joueursListe.stream()
					.distinct()
					.collect(Collectors.toList());

			List<Base> basesList = this.extractBases(joueursListe);

			this.playerDAO.insertAll(joueursListe);
			this.baseDAO.insertAll(basesList);

			//get POI and Alliances
			int maxRankingAlliance = Integer.valueOf(this.settingDAO.getOne("maxRankingAlliance").getValue());
			List<Alliance> alliancesListe = this.getAlliancesData(maxRankingAlliance);

			//Removing duplicates
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

			//We add "no alliance" as Alliance with Id 0
			Alliance noAlliance = new Alliance();
			noAlliance.setId(new Long(0));
			alliancesListe.add(noAlliance);

			this.allianceDAO.saveAll(alliancesListe);
			this.poiDAO.saveAll(poisList);

			DateTimeZone zone = DateTimeZone.forID(account.getTimezone());
			DateTime dt = new DateTime(zone);

			//Archive
			this.playerDAO.archive(dt.toDate());
			this.baseDAO.archive(dt.toDate());
			this.allianceDAO.archive(dt.toDate());
			this.poiDAO.archive(dt.toDate());

			this.settingDAO.save(new Settings("date_last_update", formatter.print(dt)));
			this.scriptingDAO.updateCompteurs(joueursListe.size(), alliancesListe.size());
		}  

		//stats Processing
		this.calculStats();

		//clearCache on ccta stats side
		this.cnCService.clearCache(userSession.getWorldId());
	}


	public List<Player> getPlayerData(UserSession userSession, int maxRanking) {
		log.info("Get players data");
		int countPlayers = this.gameService.getRankingCount(userSession, 0, 0);

		//WORKAROUND for bug in CNC Rank when you hit the end
		countPlayers = countPlayers - 5;

		log.info("Nombre de joueurs : {}", countPlayers);

		if (countPlayers <= 0) {
			throw new BatchException("Bad Player Count");
		}


		int currentIndex = -1; 
		RankingDataResponse rankingData = null;
		int endIndex;

		/*
		 * Get player rank
		 */
		//TODO WTF WITH INDEX ? it can be simplier
		log.debug("Get player rank");
		do {
			endIndex = currentIndex + this.rankingInterval;
			//On vérifie si on a pas atteint les limites
			if (endIndex > countPlayers - 1) { endIndex =  countPlayers - 1; } 
			if (endIndex > maxRanking - 1) { endIndex = maxRanking - 1; } 

			RankingDataResponse rankDataTmp = this.gameService.getRankingData(userSession, 0, currentIndex + 1, endIndex, 2, true);
			currentIndex = endIndex; //Les joueurs traités à ajouter

			if (rankingData == null) {
				rankingData = rankDataTmp;
			} else {
				rankingData.getP().addAll(rankDataTmp.getP());
			}

			//Tant qu'on a pas fait tous les joueurs ou atteint la limite
		} while (currentIndex + 1 < maxRanking && currentIndex + 1< countPlayers); 

		List<Player> playerList = new ArrayList<>();
		
		try {
			List<CompletableFuture<Player>> futures = new ArrayList<CompletableFuture<Player>>();
			for (P p : rankingData.getP()) {
				CompletableFuture<Player> future = this.getPlayerData(userSession, p);          
				futures.add(future);
			}

			for (CompletableFuture<Player> future : futures) {
				playerList.add(future.get());
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new BatchException("Error while trying to get future data");
		}
		
		return playerList;
	}

	
	/**
	 * Get map data for tile X
	 * @param x
	 * @param serverInfos
	 * @param gameSessionId
	 * @return
	 * @throws InterruptedException
	 */
	@Async
	public CompletableFuture<Player> getPlayerData(UserSession userSession, P rankingDataP) throws InterruptedException {

      PublicPlayerInfoResponse playerInfo = this.gameService.getPublicPlayerInfoRequest(userSession, rankingDataP.getP());
      List<Base> basesList = new ArrayList<Base>();

      //get bases of player
      for (C base : playerInfo.getC()){
    	Base baseTmp = new Base(base.getI(), base.getX(), base.getY(), rankingDataP.getP(), base.getN(), base.getP());
        basesList.add(baseTmp);
      }
      
      List<Ew> ewFirstRank = new ArrayList<Ew>();
      if (playerInfo.getEw() != null){
        for (Ew serverWon : playerInfo.getEw()) {
          if (serverWon.getR() == 1) {
            ewFirstRank.add(serverWon);
          }
        }
      }
      
      Player playerTmp = new Player(rankingDataP.getP(), playerInfo.getBde(), playerInfo.getBd(), Integer.valueOf((int) Math.round(playerInfo.getDccc())),
    		  rankingDataP.getF(), rankingDataP.getA(), ewFirstRank.size(), rankingDataP.getPn(), rankingDataP.getR(),
    		  rankingDataP.getS(), playerInfo.getBde() + playerInfo.getBd(), basesList);
          

		return CompletableFuture.completedFuture(playerTmp);
	}
	
}
