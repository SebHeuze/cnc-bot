package org.cnc.cncbot.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.cnc.cncbot.config.DBContext;
import org.cnc.cncbot.dto.ResponseType;
import org.cnc.cncbot.dto.UserSession;
import org.cnc.cncbot.dto.cctastats.JoueursRegistered;
import org.cnc.cncbot.dto.rankingdata.A;
import org.cnc.cncbot.dto.rankingdata.P;
import org.cnc.cncbot.dto.rankingdata.RankingDataResponse;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.map.dao.DAOConstants;
import org.cnc.cncbot.service.retrofit.CctaStatsService;
import org.cnc.cncbot.service.retrofit.ServiceGenerator;
import org.cnc.cncbot.stats.async.StatsAsyncTasks;
import org.cnc.cncbot.stats.dao.StatsAccountDAO;
import org.cnc.cncbot.stats.dao.StatsAllianceDAO;
import org.cnc.cncbot.stats.dao.StatsBaseDAO;
import org.cnc.cncbot.stats.dao.StatsBatchLogDAO;
import org.cnc.cncbot.stats.dao.StatsDAO;
import org.cnc.cncbot.stats.dao.StatsListDAO;
import org.cnc.cncbot.stats.dao.StatsLogDAO;
import org.cnc.cncbot.stats.dao.StatsPlayerDAO;
import org.cnc.cncbot.stats.dao.StatsPoiDAO;
import org.cnc.cncbot.stats.dao.StatsProcessingDAO;
import org.cnc.cncbot.stats.dao.StatsSettingsDAO;
import org.cnc.cncbot.stats.entities.Stat;
import org.cnc.cncbot.stats.entities.StatsAccount;
import org.cnc.cncbot.stats.entities.StatsAlliance;
import org.cnc.cncbot.stats.entities.StatsBase;
import org.cnc.cncbot.stats.entities.StatsBatchLog;
import org.cnc.cncbot.stats.entities.StatsList;
import org.cnc.cncbot.stats.entities.StatsLog;
import org.cnc.cncbot.stats.entities.StatsPlayer;
import org.cnc.cncbot.stats.entities.StatsPoi;
import org.cnc.cncbot.stats.entities.StatsSettings;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Map service
 * @author sheuze
 *
 */
@Service
@Setter
@Slf4j
public class StatsService {

	private final StatsAsyncTasks asyncTasks;
	
	private final GameService gameService;

	private final StatsAccountDAO accountDAO;
	private final StatsBatchLogDAO batchLogDAO;
	private final StatsListDAO statsListDAO;
	private final StatsLogDAO statsLogDAO;
	
	private final StatsProcessingDAO statsProcessingDAO;
	
	private final StatsSettingsDAO settingDAO;
	private final StatsDAO statsDAO;
	private final StatsAllianceDAO allianceDAO;
	private final StatsPlayerDAO playerDAO;
	private final StatsBaseDAO baseDAO;
	private final StatsPoiDAO poiDAO;
	private final org.cnc.cncbot.map.dao.PoiDAO poiDAOMap;

	private final CctaStatsService cctaStatsService;

	/**
	 * Intervalle de récupération pour les rangs.
	 */
	@Value("${cncbot.stats.ranking_interval}")
	private int rankingInterval; 

	/**
	 * Url CCTA Stats
	 */
	private String cctaStatsHost;

	/**
	 * Formatteur de date SQL.
	 */
	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

	@Autowired
	public StatsService(
			@Value("${cncbot.stats.host}") String cctaStatsHost,
			StatsAsyncTasks asyncTasks,
			GameService gameService, StatsAccountDAO accountDAO, StatsBatchLogDAO batchLogDAO, StatsSettingsDAO settingDAO,
			StatsListDAO statsListDAO, StatsProcessingDAO statsProcessingDAO, StatsLogDAO statsLogDAO,
			StatsDAO statsDAO, StatsAllianceDAO allianceDAO, StatsPlayerDAO playerDAO, StatsBaseDAO baseDAO, StatsPoiDAO poiDAO, org.cnc.cncbot.map.dao.PoiDAO poiDAOMap) {
		this.cctaStatsHost = cctaStatsHost;
		
		this.asyncTasks = asyncTasks;
		this.gameService = gameService;
		this.accountDAO = accountDAO;
		this.batchLogDAO = batchLogDAO;
		this.settingDAO = settingDAO;
		this.statsListDAO = statsListDAO;
		this.statsLogDAO =  statsLogDAO;
		
		this.statsProcessingDAO = statsProcessingDAO;

		this.statsDAO = statsDAO;
		this.allianceDAO = allianceDAO;
		this.playerDAO = playerDAO;
		this.baseDAO = baseDAO;
		this.poiDAO = poiDAO;
		this.poiDAOMap = poiDAOMap;
		

		this.cctaStatsService = ServiceGenerator.createService(CctaStatsService.class, this.cctaStatsHost, ResponseType.JSON);
	}

	/**
	 * Main method for Stats Batch
	 * @throws BatchException
	 */
	public void statsJob() throws BatchException {
		DBContext.setDatasource("cctastats");

		log.info("Launch of stats batch");

		List<StatsAccount> accountList = this.accountDAO.findByActiveTrue();

		StatsBatchLog batchLog = new StatsBatchLog();
		batchLog.setDateDebut(new Date());
		batchLog.setNbWorlds(accountList.size());

		batchLog = this.batchLogDAO.save(batchLog);

		log.info("Accounts size {}", accountList.size());

		int nbFails = 0;
		String failList = "";
		for (StatsAccount account : accountList) {
			DBContext.setSchema(DAOConstants.SCHEMA_PREFIX + account.getWorldId()); 
			Optional<StatsSettings> updateDateSetting = this.settingDAO.findById("date_last_update");
			
			if (!updateDateSetting.isPresent()) {
				log.error("Error : no update date setting, end of stats batch for World {}", account.getWorldId());
				break;
			} 
			
			DateTimeZone zone = DateTimeZone.forID(account.getTimezone());
			DateTime dt = new DateTime(zone);
			String currentDateTimezone = formatter.print(dt);

			if (!currentDateTimezone.equals(updateDateSetting.get().getValue())){
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

				Optional<StatsSettings> forceStatsSetting = this.settingDAO.findById("force_stats");

				if (!forceStatsSetting.isPresent()) {
					log.error("Error : no force_stats setting, end of stats batch for World {}", account.getWorldId());
					break;
				} 
				
				if (Integer.parseInt(forceStatsSetting.get().getValue()) == 1){
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
	public void statsJobForWorld(StatsAccount account, boolean statsOnly) throws BatchException {
		log.info("Start map batch of World : {}", account.getWorldId());

		UserSession userSession = new UserSession(account.getUser(), account.getPass(), account.getWorldId(), 0, 0,
				null, "World42Dummy", null);


		//@TODO call player endpoint to get username in launchWorld
		String gameSessionId = this.gameService.launchWorld(userSession);
		this.asyncTasks.setGameService(this.gameService);
		
		userSession.setGameSessionId(gameSessionId);
		
		if(!statsOnly){
			//Deleting existing data
			this.playerDAO.truncateTable();
			this.baseDAO.truncateTable();
			this.allianceDAO.truncateTable();
			this.poiDAO.truncateTable();

			//Get players data

			Optional<StatsSettings> maxRankingJoueurSetting = this.settingDAO.findById("max_ranking_player");
			
			if (!maxRankingJoueurSetting.isPresent()) {
				throw new BatchException("max_ranking_player Setting is not present, abort");
			}
			
			List<StatsPlayer> joueursListe = this.getPlayerData(userSession, Integer.valueOf(maxRankingJoueurSetting.get().getValue()));

			// Removing duplicates
			joueursListe = joueursListe.stream()
					.distinct()
					.collect(Collectors.toList());

			List<StatsBase> basesList = this.extractBases(joueursListe);

			this.playerDAO.saveAll(joueursListe);
			this.baseDAO.saveAll(basesList);

			//get POI and Alliances
			Optional<StatsSettings> maxRankingAllianceSetting = this.settingDAO.findById("max_ranking_alliance");
			
			if (!maxRankingAllianceSetting.isPresent()) {
				throw new BatchException("max_ranking_alliance Setting is not present, abort");
			}
			
			List<StatsAlliance> alliancesListe = this.getAlliancesData(userSession, Integer.valueOf(maxRankingAllianceSetting.get().getValue()));

			//Removing duplicates
			alliancesListe = alliancesListe.stream()
					.distinct()
					.collect(Collectors.toList());

			List<StatsPoi> poisList = this.extractPois( alliancesListe);

			//We look for free POI in cncmap DB
			DBContext.setDatasource("cncmap");
			List<org.cnc.cncbot.map.entities.Poi> allPOIList = this.poiDAOMap.findAll();

			//Since we have no id we create one with coords
			poisList.addAll(allPOIList.stream()
					.map(poiMap -> new StatsPoi(poiMap.getCoords().getX()*1000 + poiMap.getCoords().getY(),
														poiMap.getCoords().getX(), poiMap.getCoords().getY(), poiMap.getAllianceId(),
														poiMap.getLevel(), poiMap.getType()))
					.distinct()
	        		.collect(Collectors.toList())); 
			
			DBContext.setDatasource("cctastats");
			

			//We add "no alliance" as Alliance with Id 0
			StatsAlliance noAlliance = new StatsAlliance(0, "", new Long(0), 0, 0, 0, 9999, 0, "No alliance", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new Long(0), 0, 0, 0, 0, 0, 0, 0, new Long(0), null);
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

			this.settingDAO.save(new StatsSettings("date_last_update", formatter.print(dt)));
			this.accountDAO.updateCounts(account, joueursListe.size(), alliancesListe.size());
		}  

		//stats Processing
		this.processStats(userSession);

		//clearCache on ccta stats side
		this.cctaStatsService.clearCache(userSession.getWorldId());
	}

	/**
	 * Process stats
	 * @param idMonde
	 * @throws IOException 
	 */
	public void processStats(UserSession userSession) {
		log.info("Starting stats process");

		this.statsDAO.truncateTable();

		//Global stats
		List<StatsList> statsListGlobal = this.statsListDAO.findAllByType(0);

		//For each stats
		for (StatsList stat : statsListGlobal) {
			long startTime = System.currentTimeMillis();
			
			JsonArray jsonResult = this.statsProcessingDAO.excecuteStat(userSession, stat, null);
			this.statsDAO.save(new Stat(null, jsonResult.toString(), 0, stat.getName()));
			
		    long endTime = System.currentTimeMillis();
		    Long duree = endTime -startTime;
		    
		    this.statsLogDAO.save(new StatsLog(null, stat.getId(), duree.intValue(), new Date(), userSession.getWorldId(), 0));
		}

		//Get Stats by Alliance
		List<StatsList> statsListAlliance = this.statsListDAO.findAllByType(1);

		try {
			log.debug("Find Players that are registered on ccta stats");
			Call<List<JoueursRegistered>> listeIdJoueursCall = this.cctaStatsService.getPlayersRegistered(userSession.getWorldId());
			Response<List<JoueursRegistered>> listeIdJoueursResponse = listeIdJoueursCall.execute();			
			log.debug("End of Player find");
	
			if (listeIdJoueursResponse.body().size() > 0){
				List<Integer> playersId = listeIdJoueursResponse.body().stream()
				        .filter(Objects::nonNull)
				        .map(elt -> elt.getIdJoueur())
				        .collect(Collectors.toList()); 
				
				log.debug("Start get alliances");
				List<StatsAlliance> listeIdAlliances = this.playerDAO.findAlliancesOfPlayers(playersId);
				log.debug("End get alliances");
				
				//For each alliance
				for (StatsAlliance alliance : listeIdAlliances){
					//Pour chaque stat
					log.debug("Process stats alliance {}", alliance);
					for (StatsList stat : statsListAlliance) {
						long startTime = System.currentTimeMillis();
						JsonArray jsonResult = this.statsProcessingDAO.excecuteStat(userSession, stat, alliance);
						this.statsDAO.save(new Stat(null, jsonResult.toString(), alliance.getId(), stat.getName()));
						
						long endTime = System.currentTimeMillis();
					    Long duree = endTime - startTime;
					    this.statsLogDAO.save(new StatsLog(null, stat.getId(), duree.intValue(), new Date(), userSession.getWorldId(), alliance.getId()));
					}
				}
			} else {
				log.info("No alliance stats to process");
			}
	
			this.settingDAO.updateSetting("force_stats", "0");
		} catch (IOException e) {
			log.error("Cannot access cctastats service to get Player list, stats processing stopped", e);
		}
	}

	/***
	 * Get detailled alliance Data
	 * @param userSession
	 * @param maxRanking
	 * @return
	 */
	public List<StatsAlliance> getAlliancesData(UserSession userSession, int maxRanking) {

		log.info("Get alliances data");
		int countAlliances = this.gameService.getRankingCount(userSession, 1, 0);

		log.info("Alliances count: {}", countAlliances);

		if (countAlliances <= 0){
			throw new BatchException("Error with alliance rank data");
		}

		int currentIndex = 0;
		int endIndex;


		RankingDataResponse rankingData = null;
		/*
		 * Get full rankings
		 */
		log.debug("Get alliance rank");

		do {
			endIndex = currentIndex + (this.rankingInterval - 1);
			//On vérifie si on a pas atteint les limites
			if (endIndex > countAlliances - 1) { endIndex =  countAlliances - 1; } 
			if (endIndex > maxRanking - 1) { endIndex = maxRanking - 1; } 

			RankingDataResponse rankDataTmp = this.gameService.getRankingData(userSession, 1, currentIndex, endIndex, 2, true);
			currentIndex = endIndex + 1; 

			if (rankingData == null) {
				rankingData = rankDataTmp;
			} else {
				rankingData.getA().addAll(rankDataTmp.getA());
			}

		} while (currentIndex < maxRanking - 1 && currentIndex < countAlliances - 1); 


		List<StatsAlliance> alliancesList = new ArrayList<>();

		try {
			List<CompletableFuture<StatsAlliance>> futures = new ArrayList<CompletableFuture<StatsAlliance>>();
			for (A a : rankingData.getA()) {
				CompletableFuture<StatsAlliance> future = this.asyncTasks.getAlliancePublicInfos(userSession, a);          
				futures.add(future);
			}
			CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
			for (CompletableFuture<StatsAlliance> future : futures) {
				alliancesList.add(future.get());
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new BatchException("Error while trying to get future data");
		}

		return alliancesList;
	}


	/**
	 * Get detailled player data
	 * @param userSession
	 * @param maxRanking
	 * @return
	 */
	public List<StatsPlayer> getPlayerData(UserSession userSession, int maxRanking) {
		log.info("Get players data");
		int countPlayers = this.gameService.getRankingCount(userSession, 0, 0);

		//WORKAROUND for bug in CNC Rank when you hit the end
		countPlayers = countPlayers - 5;

		log.info("Players count : {}", countPlayers);

		if (countPlayers <= 0) {
			throw new BatchException("Bad Player Count");
		}


		int currentIndex = 0; 
		int endIndex;

		RankingDataResponse rankingData = null;

		/*
		 * Get player rank
		 */
		log.debug("Get player rank");
		do {
			endIndex = currentIndex + (this.rankingInterval - 1);
			//On vérifie si on a pas atteint les limites
			if (endIndex > countPlayers - 1) { endIndex =  countPlayers - 1; } 
			if (endIndex > maxRanking - 1) { endIndex = maxRanking - 1; } 

			RankingDataResponse rankDataTmp = this.gameService.getRankingData(userSession, 0, currentIndex, endIndex, 2, true);
			currentIndex = endIndex + 1; //Les joueurs traités à ajouter

			if (rankingData == null) {
				rankingData = rankDataTmp;
			} else {
				rankingData.getP().addAll(rankDataTmp.getP());
			}

			//Tant qu'on a pas fait tous les joueurs ou atteint la limite
		} while (currentIndex < maxRanking - 1 && currentIndex < countPlayers - 1); 

		List<StatsPlayer> playerList = new ArrayList<>();

		try {
			List<CompletableFuture<StatsPlayer>> futures = new ArrayList<CompletableFuture<StatsPlayer>>();
			for (P p : rankingData.getP()) {
				CompletableFuture<StatsPlayer> future = this.asyncTasks.getPlayerPublicInfos(userSession, p);          
				futures.add(future);
			}

			CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
			
			for (CompletableFuture<StatsPlayer> future : futures) {
				CompletableFuture.allOf(future);
				playerList.add(future.get());
			}
		} catch (InterruptedException | ExecutionException e) {
			log.error("Error while trying to get future data", e);
			throw new BatchException("Error while trying to get future data");
		}

		return playerList;
	}


	

	/**
	 * Extract all bases from players
	 * @param playersListe
	 * @return
	 */
	public List<StatsBase> extractBases(List<StatsPlayer> playersListe) {
		List<StatsBase> bases = new ArrayList<StatsBase>();
		for (StatsPlayer player : playersListe) {
			bases.addAll(player.getBases());
			//We delete bases from player objet to insert them later (performance issue fix)
			player.setBases(null);
		}
		return bases;
	}


	/**
	 * Extract all Pois from alliances
	 * @param alliancesListe
	 * @return
	 */
	public List<StatsPoi> extractPois(List<StatsAlliance> alliancesListe) {
		List<StatsPoi> pois = new ArrayList<StatsPoi>();
		for (StatsAlliance alliance : alliancesListe) {
			pois.addAll(alliance.getPoiList());
			//We delete pois from alliance objet to insert them later (performance issue fix)
			alliance.setPoiList(null);
		}
		return pois;
	}

}
