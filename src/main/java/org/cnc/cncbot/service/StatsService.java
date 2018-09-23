package org.cnc.cncbot.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.cnc.cncbot.config.DBContext;
import org.cnc.cncbot.dto.ResponseType;
import org.cnc.cncbot.dto.UserSession;
import org.cnc.cncbot.dto.cctastats.JoueursRegistered;
import org.cnc.cncbot.dto.publicallianceinfo.Opoi;
import org.cnc.cncbot.dto.publicallianceinfo.PublicAllianceInfoResponse;
import org.cnc.cncbot.dto.publicallianceinfo.Rpoi;
import org.cnc.cncbot.dto.publicplayerinfo.C;
import org.cnc.cncbot.dto.publicplayerinfo.Ew;
import org.cnc.cncbot.dto.publicplayerinfo.PublicPlayerInfoResponse;
import org.cnc.cncbot.dto.rankingdata.A;
import org.cnc.cncbot.dto.rankingdata.P;
import org.cnc.cncbot.dto.rankingdata.RankingDataResponse;
import org.cnc.cncbot.dto.serverinfos.ServerInfoResponse;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.map.dao.DAOConstants;
import org.cnc.cncbot.service.retrofit.CctaStatsService;
import org.cnc.cncbot.service.retrofit.ServiceGenerator;
import org.cnc.cncbot.stats.dao.StatsAccountDAO;
import org.cnc.cncbot.stats.dao.StatsAllianceDAO;
import org.cnc.cncbot.stats.dao.StatsBaseDAO;
import org.cnc.cncbot.stats.dao.StatsBatchLogDAO;
import org.cnc.cncbot.stats.dao.StatsPlayerDAO;
import org.cnc.cncbot.stats.dao.StatsPoiDAO;
import org.cnc.cncbot.stats.dao.StatsSettingsDAO;
import org.cnc.cncbot.stats.dao.StatsDAO;
import org.cnc.cncbot.stats.dao.StatsListDAO;
import org.cnc.cncbot.stats.dao.StatsLogDAO;
import org.cnc.cncbot.stats.dao.StatsProcessingDAO;
import org.cnc.cncbot.stats.entities.StatsAccount;
import org.cnc.cncbot.stats.entities.StatsAlliance;
import org.cnc.cncbot.stats.entities.StatsBase;
import org.cnc.cncbot.stats.entities.StatsBatchLog;
import org.cnc.cncbot.stats.entities.StatsPlayer;
import org.cnc.cncbot.stats.entities.StatsPoi;
import org.cnc.cncbot.stats.entities.StatsSettings;
import org.cnc.cncbot.stats.entities.Stat;
import org.cnc.cncbot.stats.entities.StatsList;
import org.cnc.cncbot.stats.entities.StatsLog;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
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
			GameService gameService, StatsAccountDAO accountDAO, StatsBatchLogDAO batchLogDAO, StatsSettingsDAO settingDAO,
			StatsListDAO statsListDAO, StatsProcessingDAO statsProcessingDAO, StatsLogDAO statsLogDAO,
			StatsDAO statsDAO, StatsAllianceDAO allianceDAO, StatsPlayerDAO playerDAO, StatsBaseDAO baseDAO, StatsPoiDAO poiDAO, org.cnc.cncbot.map.dao.PoiDAO poiDAOMap) {
		this.cctaStatsHost = cctaStatsHost;
		
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

		List<StatsAccount> accountList = this.accountDAO.findAll();

		StatsBatchLog batchLog = new StatsBatchLog();
		batchLog.setDateDebut(new Date());
		batchLog.setNbWorlds(accountList.size());

		batchLog = this.batchLogDAO.save(batchLog);

		log.info("Accounts size {}", accountList.size());

		int nbFails = 0;
		String failList = "";
		for (StatsAccount account : accountList) {
			DBContext.setSchema(DAOConstants.SCHEMA_PREFIX + account.getWorldId()); 
			StatsSettings updateDate = this.settingDAO.getOne("date_last_update");

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

				StatsSettings forceStats = this.settingDAO.getOne("force_stats");

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
	public void statsJobForWorld(StatsAccount account, boolean statsOnly) throws BatchException {
		log.info("Start map batch of World : {}", account.getWorldId());

		UserSession userSession = new UserSession(account.getUser(), account.getPass(), account.getWorldId(), 0, 0,
				null, "World42Dummy", null);


		//@TODO call player endpoint to get username in launchWorld
		String gameSessionId = this.gameService.launchWorld(userSession);

		userSession.setGameSessionId(gameSessionId);
		
		if(!statsOnly){
			//Deleting existing data
			this.playerDAO.truncateTable();
			this.baseDAO.truncateTable();
			this.allianceDAO.truncateTable();
			this.poiDAO.truncateTable();

			//Get players data

			int maxRankingJoueur = Integer.valueOf(this.settingDAO.getOne("maxRankingJoueur").getValue());
			List<StatsPlayer> joueursListe = this.getPlayerData(userSession, maxRankingJoueur);

			// Removing duplicates
			joueursListe = joueursListe.stream()
					.distinct()
					.collect(Collectors.toList());

			List<StatsBase> basesList = this.extractBases(joueursListe);

			this.playerDAO.saveAll(joueursListe);
			this.baseDAO.saveAll(basesList);

			//get POI and Alliances
			int maxRankingAlliance = Integer.valueOf(this.settingDAO.getOne("maxRankingAlliance").getValue());
			List<StatsAlliance> alliancesListe = this.getAlliancesData(userSession, maxRankingAlliance);

			//Removing duplicates
			alliancesListe = alliancesListe.stream()
					.distinct()
					.collect(Collectors.toList());

			List<StatsPoi> poisList = this.extractPois( alliancesListe);

			//We look for free POI in cncmap DB
			DBContext.setDatasource("cncmap");
			List<org.cnc.cncbot.map.entities.Poi> allPOIList = this.poiDAOMap.findAll();

			//Since we have no id we create one with coords
			poisList = allPOIList.stream()
					.map(poiMap -> new StatsPoi(new Long(poiMap.getCoords().getX()*1000 + poiMap.getCoords().getY()),
														poiMap.getCoords().getX(), poiMap.getCoords().getY(), poiMap.getAllianceId(),
														poiMap.getLevel(), poiMap.getType()))
					.distinct()
	        		.collect(Collectors.toList()); 
			
			DBContext.setDatasource("cctastats");
			

			//We add "no alliance" as Alliance with Id 0
			StatsAlliance noAlliance = new StatsAlliance();
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
			
			JsonArray jsonResult = this.statsProcessingDAO.excecuteStat(stat, null);
			this.statsDAO.save(new Stat(null, jsonResult.toString(), 0, stat.getName()));
			
		    long endTime = System.currentTimeMillis();
		    Long duree = endTime -startTime;
		    
		    this.statsLogDAO.save(new StatsLog(null, stat.getId(), duree.intValue(), new Date(), userSession.getWorldId(), new Long(0)));
		}

		//Get Stats by Alliance
		List<StatsList> statsListAlliance = this.statsListDAO.findAllByType(0);

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
						this.statsProcessingDAO.excecuteStat(stat, alliance); 
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
				CompletableFuture<StatsAlliance> future = this.getAlliancePublicInfos(userSession, a);          
				futures.add(future);
			}

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
				CompletableFuture<StatsPlayer> future = this.getPlayerPublicInfos(userSession, p);          
				futures.add(future);
			}

			for (CompletableFuture<StatsPlayer> future : futures) {
				playerList.add(future.get());
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new BatchException("Error while trying to get future data");
		}

		return playerList;
	}


	/**
	 * Get Player public detailled infos
	 * @param userSession
	 * @param rankingDataP
	 * @return
	 * @throws InterruptedException
	 */
	@Async
	public CompletableFuture<StatsPlayer> getPlayerPublicInfos(UserSession userSession, P rankingDataP) throws InterruptedException {

		PublicPlayerInfoResponse playerInfo = this.gameService.getPublicPlayerInfoRequest(userSession, rankingDataP.getP());
		List<StatsBase> basesList = new ArrayList<StatsBase>();

		//get bases of player
		for (C base : playerInfo.getC()){
			StatsBase baseTmp = new StatsBase(base.getI(), base.getX(), base.getY(), rankingDataP.getP(), base.getN(), base.getP());
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

		StatsPlayer playerTmp = new StatsPlayer(rankingDataP.getP(), playerInfo.getBde(), playerInfo.getBd(), Integer.valueOf((int) Math.round(playerInfo.getDccc())),
				rankingDataP.getF(), rankingDataP.getA(), ewFirstRank.size(), rankingDataP.getPn(), rankingDataP.getR(),
				rankingDataP.getS(), playerInfo.getBde() + playerInfo.getBd(), basesList);


		return CompletableFuture.completedFuture(playerTmp);
	}

	/**
	 * Get public alliance infos
	 * @param rankingDataA
	 * @return
	 */
	@Async
	public CompletableFuture<StatsAlliance> getAlliancePublicInfos(UserSession userSession, A rankingDataA) {
		List<StatsAlliance> result = new ArrayList<StatsAlliance>(); // this is some custom bean holding your result

		PublicAllianceInfoResponse allianceInfo = this.gameService.getPublicAllianceInfoRequest(userSession, rankingDataA.getA());

		//We get rank and POI Score
		List<Rpoi> listeInfosPois = allianceInfo.getRpois();
		int[] rangsPoi = new int[8];
		int[] scoresPoi = new int[8];
		int indexTmp = 1;
		for (Rpoi poiInfo : listeInfosPois) {
			rangsPoi[indexTmp] = poiInfo.getR();
			scoresPoi[indexTmp] = poiInfo.getS();
			indexTmp++;
		}

		List<StatsPoi> listePois = new ArrayList<StatsPoi>();

		//Get alliance Poi
		for (Opoi poi : allianceInfo.getOpois()) {
			StatsPoi poiTmp = new StatsPoi(new Long(poi.getI()), poi.getX(), poi.getY(), rankingDataA.getA(), poi.getL(), poi.getT()-1);
			listePois.add(poiTmp);
		}
		StatsAlliance allianceTmp = 
				new StatsAlliance(new Long(rankingDataA.getA()), allianceInfo.getD(), rankingDataA.getSa(), allianceInfo.getBdp(),
						allianceInfo.getBde(), allianceInfo.getBd(), 0, 
						allianceInfo.getPoi(), rankingDataA.getAn(), rankingDataA.getBc(), rankingDataA.getPc(), rankingDataA.getR(),
						rangsPoi[1], rangsPoi[2], rangsPoi[3], rangsPoi[4], rangsPoi[5],
						rangsPoi[6], rangsPoi[7], rankingDataA.getSc(), scoresPoi[1], scoresPoi[2],
						scoresPoi[3], scoresPoi[4], scoresPoi[5], scoresPoi[6], scoresPoi[7],
						rankingDataA.getS(), listePois);

		result.add(allianceTmp);

		return CompletableFuture.completedFuture(allianceTmp);
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
		}
		return pois;
	}

}
