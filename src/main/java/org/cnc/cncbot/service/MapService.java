package org.cnc.cncbot.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.cnc.cncbot.config.DBContext;
import org.cnc.cncbot.dto.UserSession;
import org.cnc.cncbot.dto.serverinfos.ServerInfoResponse;
import org.cnc.cncbot.exception.AuthException;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.exception.EAAuthException;
import org.cnc.cncbot.map.async.MapAsyncTasks;
import org.cnc.cncbot.map.dao.AccountDAO;
import org.cnc.cncbot.map.dao.AllianceDAO;
import org.cnc.cncbot.map.dao.BaseDAO;
import org.cnc.cncbot.map.dao.DAOConstants;
import org.cnc.cncbot.map.dao.EndGameDAO;
import org.cnc.cncbot.map.dao.PlayerDAO;
import org.cnc.cncbot.map.dao.PoiDAO;
import org.cnc.cncbot.map.dao.SettingsDAO;
import org.cnc.cncbot.map.dto.MapData;
import org.cnc.cncbot.map.entities.Account;
import org.cnc.cncbot.map.entities.Alliance;
import org.cnc.cncbot.map.entities.Base;
import org.cnc.cncbot.map.entities.EndGame;
import org.cnc.cncbot.map.entities.MapObject;
import org.cnc.cncbot.map.entities.Player;
import org.cnc.cncbot.map.entities.Poi;
import org.cnc.cncbot.map.entities.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Map service
 * @author SebHeuze
 *
 */
@Service
@Setter
@Slf4j
public class MapService {


	public final GameService gameService;

	public final MapAsyncTasks asyncTasks;
	
	public final AccountDAO accountDAO;
	public final PoiDAO poiDao;
	public final AllianceDAO allianceDao;
	public final PlayerDAO playerDao;
	public final BaseDAO baseDao;
	public final EndGameDAO endGameDao;
	public final SettingsDAO settingsDao;

	@Autowired
	public MapService(MapAsyncTasks asyncTasks, GameService gameService, SettingsDAO settingsDao, 
			PoiDAO poiDao, AllianceDAO allianceDao, PlayerDAO playerDao, BaseDAO baseDao, EndGameDAO endGameDao, AccountDAO accountDAO) {
		this.asyncTasks = asyncTasks;
		this.accountDAO = accountDAO;
		this.gameService = gameService;
		this.poiDao = poiDao;
		this.allianceDao = allianceDao;
		this.playerDao = playerDao;
		this.baseDao = baseDao;
		this.endGameDao = endGameDao;
		this.settingsDao = settingsDao;
	}

	/**
	 * Main method for Map Batch
	 * @param batchNumber
	 * @throws BatchException
	 */
	public void mapDataJob(int batchNumber) throws BatchException {
		DBContext.setDatasource("cncmap");
		
		//Get account with batchNumber, batch number is currently the periodicity of the batch (batch 5 is called every 5 min)
		List<Account> accountList = this.accountDAO.findByNumbatchAndActiveTrue(batchNumber);
		log.info("accounts retrieved : {}", accountList.size());

		for (Account account : accountList) {
			try {
				this.mapForAccount(account);
			} catch (AuthException ae){
				log.error("Error during auth step with account {}", account.getUser(), ae);
			} catch (EAAuthException eae) {
				//Disable account by setting active to null (to be able to distinguish from manually disabled accounts)
				log.error("Error while doing EA auth {}", eae);
				account.setActive(null);
				this.accountDAO.save(account);
			}
		}

	}

	/**
	 * Retrieve map data for account
	 * @param account
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void mapForAccount(Account account) {
		log.info("Start map batch of World : {}", account.getWorldId());

		//TODO: FIX username
		UserSession userSession = new UserSession(account.getUser(), account.getPass(), account.getWorldId(), 0, 0,
				null, "World42Dummy", null);

		String gameSessionId = this.gameService.launchWorld(userSession);
		userSession.setGameSessionId(gameSessionId);
		
		userSession.setPlayerName(this.gameService.getPlayerInfo(userSession).getName());
		this.asyncTasks.setGameService(this.gameService);
		
		
		ServerInfoResponse serverInfos = this.gameService.getServerInfos(userSession);
		Set<Alliance> alliancesListTotal = new HashSet<Alliance>();
		alliancesListTotal.add(new Alliance(0, "No Alliance", new Long(0), 0));
		Set<Player> playersListTotal = new HashSet<Player>();
		Set<MapObject> listeObjectMap = new HashSet<MapObject>();

		//Server Size (ex 1100) / Bloc Size (32) = 34.25
		//Need 35 request to get the full World info
		try {
			List<CompletableFuture<MapData>> futures = new ArrayList<CompletableFuture<MapData>>();
			for (int x = 1; x < (int) (serverInfos.getWw() / MapAsyncTasks.SIZE_TILE) + 1; x++) {
				CompletableFuture<MapData> future = this.asyncTasks.getMapDataTile(x, serverInfos, userSession);          
				futures.add(future);
			}

			CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
			for (CompletableFuture<MapData> future : futures) {
				alliancesListTotal.addAll(new ArrayList<Alliance>(future.get().getAlliancesList()));
				playersListTotal.addAll(new ArrayList<Player>(future.get().getPlayersList()));
				listeObjectMap.addAll(future.get().getObjectsList());
			}
		} catch (InterruptedException | ExecutionException e) {
			log.error("Error while trying to get future data", e);
			throw new BatchException("Error while trying to get future data");
		}


		//Get POI/Bases from the list
		List<Base> listeBase =  listeObjectMap.stream().filter(p-> p instanceof Base).map(obj -> (Base) obj).collect(Collectors.toList());
		List<Poi> listePOI = listeObjectMap.stream().filter(p-> p instanceof Poi).map(obj -> (Poi) obj).collect(Collectors.toList());
		List<EndGame> listeEndGames = listeObjectMap.stream().filter(p-> p instanceof EndGame).map(obj -> (EndGame) obj).collect(Collectors.toList());


		log.info("Data retrieved : Base {}/Poi {}/EndGame {}/Alliance {}/Player {}", 
				listeBase.size(), listePOI.size(), listeEndGames.size(), alliancesListTotal.size(), playersListTotal.size());

		log.info("Saving data of World : {}", account.getWorldId());
		/*
		 * Save Data in DB 
		 */
		DBContext.setSchema(DAOConstants.SCHEMA_PREFIX + account.getWorldId());
		//Delete all the actual data
		this.allianceDao.truncateTable();
		this.playerDao.truncateTable();
		this.poiDao.truncateTable();
		this.baseDao.truncateTable();
		this.endGameDao.truncateTable();

		this.poiDao.saveAll(listePOI);
		this.baseDao.saveAll(listeBase);
		this.endGameDao.saveAll(listeEndGames);
		this.allianceDao.saveAll(alliancesListTotal);
		this.playerDao.saveAll(playersListTotal);
		
		this.allianceDao.updateNbJoueurs();

		this.settingsDao.deleteAll();
		this.settingsDao.save(new Settings("timestamp",  String.valueOf(System.currentTimeMillis() / 1000)));
	}


	



}
