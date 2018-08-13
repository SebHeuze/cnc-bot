package org.cnc.cncbot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.cnc.cncbot.config.DBContext;
import org.cnc.cncbot.dto.generated.OriginAccountInfo;
import org.cnc.cncbot.dto.generated.PollWorld;
import org.cnc.cncbot.dto.generated.S;
import org.cnc.cncbot.dto.generated.Server;
import org.cnc.cncbot.dto.serverinfos.ServerInfoResponse;
import org.cnc.cncbot.exception.AuthException;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.map.dao.AccountDAO;
import org.cnc.cncbot.map.dao.AllianceDAO;
import org.cnc.cncbot.map.dao.BaseDAO;
import org.cnc.cncbot.map.dao.DAOConstants;
import org.cnc.cncbot.map.dao.EndGameDAO;
import org.cnc.cncbot.map.dao.PlayerDAO;
import org.cnc.cncbot.map.dao.PoiDAO;
import org.cnc.cncbot.map.dao.SettingsDAO;
import org.cnc.cncbot.map.dto.DecryptResult;
import org.cnc.cncbot.map.dto.MapData;
import org.cnc.cncbot.map.dto.UserSession;
import org.cnc.cncbot.map.entities.Account;
import org.cnc.cncbot.map.entities.Alliance;
import org.cnc.cncbot.map.entities.Base;
import org.cnc.cncbot.map.entities.Coords;
import org.cnc.cncbot.map.entities.EndGame;
import org.cnc.cncbot.map.entities.MapObject;
import org.cnc.cncbot.map.entities.Player;
import org.cnc.cncbot.map.entities.Poi;
import org.cnc.cncbot.map.entities.Settings;
import org.cnc.cncbot.map.utils.CryptoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class MapService {

	/**
	 * Tag for WORLD data.
	 */
	public static final String TAG_WORLD = "WORLD";

	/**
	 * Size X map.
	 */
	public static final int SIZE_TILE = 32;

	/**
	 * Tag for ENDGAME data.
	 */
	public static final String TAG_ENDGAME = "ENDGAME";




	public final AccountService accountService;

	public final GameService gameService;

	public final AccountDAO accountDAO;
	public final PoiDAO poiDao;
	public final AllianceDAO allianceDao;
	public final PlayerDAO playerDao;
	public final BaseDAO baseDao;
	public final EndGameDAO endGameDao;
	public final SettingsDAO settingsDao;

	@Autowired
	public MapService(AccountService accountService, GameService gameService, SettingsDAO settingsDao, 
			PoiDAO poiDao, AllianceDAO allianceDao, PlayerDAO playerDao, BaseDAO baseDao, EndGameDAO endGameDao, AccountDAO accountDAO) {
		this.accountService = accountService;
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
				
				//Disable account by setting active to null (to be able to distinguish from manually disabled accounts)
				account.setActive(null);
				this.accountDAO.save(account);
			}
		}

	}

	/**
	 * Retrieve map data for account
	 * @param account
	 */
	@Transactional
	public void mapForAccount(Account account) {
		log.info("Start map batch of World : {}", account.getMonde());
		String gameSessionId = this.gameService.launchWorld(account);
		UserSession userSession = new UserSession(0, 0, gameSessionId, "World42Dummy", this.accountService.getOriginAccountInfo(account).getSessionGUID());

		ServerInfoResponse serverInfos = this.gameService.getServerInfos(userSession.getGameSessionId());
		Set<Alliance> alliancesListTotal = new HashSet<Alliance>();
		alliancesListTotal.add(new Alliance(0, "No Alliance", new Long(0), 0));
		Set<Player> playersListTotal = new HashSet<Player>();
		Set<MapObject> listeObjectMap = new HashSet<MapObject>();

		//Server Size (ex 1100) / Bloc Size (32) = 34.25
		//Need 35 request to get the full World info
		try {
			List<CompletableFuture<MapData>> futures = new ArrayList<CompletableFuture<MapData>>();
			for (int x = 1; x < (int) (serverInfos.getWw() / SIZE_TILE) + 1; x++) {
				CompletableFuture<MapData> future = this.getMapDataTile(x, serverInfos, userSession);          
				futures.add(future);
			}

			for (CompletableFuture<MapData> future : futures) {
				alliancesListTotal.addAll(new ArrayList<Alliance>(future.get().getAlliancesList()));
				playersListTotal.addAll(new ArrayList<Player>(future.get().getPlayersList()));
				listeObjectMap.addAll(future.get().getObjectsList());
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new BatchException("Error while trying to get future data");
		}


		//Get POI/Bases from the list
		List<Base> listeBase =  listeObjectMap.stream().filter(p-> p instanceof Base).map(obj -> (Base) obj).collect(Collectors.toList());
		List<Poi> listePOI = listeObjectMap.stream().filter(p-> p instanceof Poi).map(obj -> (Poi) obj).collect(Collectors.toList());
		List<EndGame> listeEndGames = listeObjectMap.stream().filter(p-> p instanceof EndGame).map(obj -> (EndGame) obj).collect(Collectors.toList());


		log.info("Data retrieved : Base {}/Poi {}/EndGame {}/Alliance {}/Player {}", 
				listeBase.size(), listePOI.size(), listeEndGames.size(), alliancesListTotal.size(), playersListTotal.size());

		log.info("Saving data of World : {}", account.getMonde());
		/*
		 * Save Data in DB 
		 */
		DBContext.setSchema(DAOConstants.SCHEMA_PREFIX + account.getMonde());
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


	/**
	 * Get map data for tile X
	 * @param x
	 * @param serverInfos
	 * @param gameSessionId
	 * @return
	 * @throws InterruptedException
	 */
	@Async
	public CompletableFuture<MapData> getMapDataTile(int x , ServerInfoResponse serverInfos, UserSession userSession) throws InterruptedException {
		//Objets Data
		List<MapObject> mapObjectList = new ArrayList<MapObject>();
		Map<Integer, Alliance> alliancesList = new HashMap<Integer, Alliance>();
		Map<Integer, Player> playersList = new HashMap<Integer, Player>();
		Set<Alliance> alliancesListTotal = new HashSet<Alliance>();
		Set<Player> playersListTotal = new HashSet<Player>();


		JsonArray jsonArray = this.gameService.poll(x, (int) (serverInfos.getWw() / SIZE_TILE) + 1, userSession);


		Gson gson = new Gson();
		PollWorld pollRequest = null;
		PollWorld pollRequestEndGame = null;

		for (JsonElement element : jsonArray) {
			JsonObject jsonObject = element.getAsJsonObject();
			String tagInfo = jsonObject.get("t").getAsString();

			log.debug("TAG {}", tagInfo);
			if (TAG_WORLD.equals(tagInfo)) {
				pollRequest = gson.fromJson(jsonObject.get("d"), PollWorld.class);
				log.debug(pollRequest.toString());
			}
			if (TAG_ENDGAME.equals(tagInfo) && pollRequestEndGame == null) {
				pollRequestEndGame = gson.fromJson(jsonObject.get("d"), PollWorld.class);
				log.debug(pollRequestEndGame.toString());
			}
		}

		if (pollRequest !=  null) {
			for (S sectorData : pollRequest.getS()) {
				alliancesList.clear();
				playersList.clear();

				int tileY = sectorData.getI() >> 8;
				int tileX = sectorData.getI() & 255;
				log.debug("Analyse du secteur X : {} Y : {}", tileX, tileY);

				alliancesList  =  this.processPollAlliances(sectorData.getA());
				alliancesListTotal.addAll(alliancesList.values());
				playersList = this.processPollJoueurs(sectorData.getP(), alliancesList);
				playersListTotal.addAll(playersList.values());
				mapObjectList.addAll(this.processPollMapObject(sectorData.getD(), alliancesList, playersList, tileX, tileY));

			}

		} 

		if(pollRequestEndGame != null) {
			mapObjectList.addAll(this.processPollEndGame(pollRequestEndGame.getCH()));
		}

		return CompletableFuture.completedFuture(new MapData(mapObjectList, alliancesListTotal, playersListTotal));
	}


	public Map<Integer, Alliance> processPollAlliances(List<String> dataAlliance) {
		Map<Integer, Alliance> alliancesList = new HashMap<Integer, Alliance>();
		for (String allianceStr : dataAlliance) {
			if (!StringUtils.isEmpty(allianceStr)) {
				//Alliance object
				Alliance allianceTmp = new Alliance();

				//Default
				allianceTmp.setPoints(new Long(100));

				// Alliance Index
				DecryptResult result = CryptoUtils.base91Decode(allianceStr, 0, 2);
				int indexAlliance = result.getResult();
				allianceStr = allianceStr.substring(2, allianceStr.length());

				//Alliance ID
				result = CryptoUtils.base91Decode(allianceStr, 0, 5);
				allianceTmp.setAllianceId(result.getResult());
				allianceStr = allianceStr.substring(result.getCurrentIndex(), allianceStr.length());

				//Unknown, I need to find out
				result = CryptoUtils.base91Decode(allianceStr, 0, 5);
				allianceStr = allianceStr.substring(result.getCurrentIndex(), allianceStr.length());
				log.debug("Unknown bumber {}",result.getResult());

				//Alliance name
				allianceTmp.setName(allianceStr);

				//Add it to the list
				log.debug("Adding alliance {}", allianceTmp.toString());
				alliancesList.put(indexAlliance, allianceTmp);
			}
		}
		return alliancesList;        
	}

	public Map<Integer, Player> processPollJoueurs(List<String> dataJoueur, Map<Integer, Alliance> alliancesList) throws BatchException {
		Map<Integer, Player> playersList = new HashMap<Integer, Player>();
		for (String joueurStr : dataJoueur){
			if(!StringUtils.isEmpty(joueurStr)){
				//Player Object
				Player joueurTmp = new Player();

				// Player index
				DecryptResult decryptResult = CryptoUtils.base91Decode(joueurStr, 0, 2);
				int indexJoueur = decryptResult.getResult();
				joueurStr = joueurStr.substring(2, joueurStr.length());

				//Player ID
				decryptResult = CryptoUtils.base91Decode(joueurStr, 0, 5);
				joueurTmp.setId(decryptResult.getResult());
				joueurStr = joueurStr.substring(decryptResult.getCurrentIndex(), joueurStr.length());

				//Unknown
				decryptResult = CryptoUtils.base91Decode(joueurStr, 0, 5);
				joueurStr = joueurStr.substring(decryptResult.getCurrentIndex(), joueurStr.length());

				//Faction (NOD/GDI)
				decryptResult = CryptoUtils.base91Decode(joueurStr, 0, 2);
				int resultTmp = decryptResult.getResult() >> 1;
				joueurTmp.setFaction(resultTmp & 3);

				//Alliance index
				int indexAlliance = resultTmp >> 2;
				if (indexAlliance > 0) {
					if (alliancesList.containsKey(indexAlliance)) {
						joueurTmp.setAllianceId(alliancesList.get(indexAlliance).getAllianceId());
					} else {
						throw new BatchException("Erreur, alliance ID doesn't exist");
					}
				}

				//Player name
				joueurTmp.setName(joueurStr.substring(decryptResult.getCurrentIndex(), joueurStr.length()));

				//We add it to the list
				log.debug("Adding player {}", joueurTmp);
				playersList.put(indexJoueur, joueurTmp);
			}
		}
		return playersList; 
	}

	/**
	 * Process Map object (Base, POI)
	 * @param dataMap
	 * @param alliancesList
	 * @param playersList
	 * @param x
	 * @param y
	 * @return
	 * @throws BatchException
	 */
	public List<MapObject> processPollMapObject(List<String> dataMap, Map<Integer, Alliance> alliancesList, Map<Integer, Player> playersList, int x, int y)
			throws BatchException {
		List<MapObject> listeMapObject = new ArrayList<MapObject>();
		for (String dataMapStr : dataMap) {
			if (!StringUtils.isEmpty(dataMapStr)) {

				//Coordinates
				DecryptResult decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 2);
				int coordData = decryptResult.getResult();
				int coordX = coordData & 31;
				coordX = coordX + 32 * x;
				coordData = coordData >> 5;
				int coordY = coordData & 31;
				coordY = coordY + 32 * y;

				//Object type (1 = Base, 4 = POI)
				int typeObjectMap = coordData >> 5;
				dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());

				Coords coords = new Coords(coordX, coordY);

				switch (typeObjectMap){
				case 1 : 
					//If 1 => Base
					Base baseObjectTmp = new Base();
					baseObjectTmp.setCoords(coords);

					//Base Data String
					decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
					int baseData = decryptResult.getResult();
					baseData = baseData >> 1;

					//LockDown
					final boolean hasLockDownEnd  = (baseData & 1) != 0;
					baseData = baseData >> 1;

					//ProtectionEnd
					final boolean hasProtectionEnd  = (baseData & 1) != 0;
					baseData = baseData >> 1;

					//SupportAlert
					final boolean hasSupportAlert  = (baseData & 1) != 0;
					baseData = baseData >> 1;

					//MoveCoolDownEndStep
					final boolean hasMoveCoolDownEndStep  = (baseData & 1) != 0;
					baseData = baseData >> 1;

					//MoveRestrictionEndStep
					final boolean hasMoveRestrictionEndStep  = (baseData & 1) != 0;
					baseData = baseData >> 1;

					//RecoveryEndStep
					final boolean hasRecoveryEndStep  = (baseData & 1) != 0;
					baseData = baseData >> 1;

					//ConditionDefense
					final boolean hasConditionDefense  = (baseData & 1) != 0;
					baseData = baseData >> 1;

					//isEndgameTarget
					final boolean isEndgameTarget  = (baseData & 1) != 0;
					baseData = baseData >> 1;

					//Niveau de la base
					baseObjectTmp.setLevel(baseData & 255);
					baseData = baseData >> 8;

					//Unknown
					//int inconnu = baseData & 15;
					baseData = baseData >> 5;

					//Player index
					int indexJoueur = baseData & 1023;
					baseObjectTmp.setPlayerId(playersList.get(indexJoueur).getId());
					baseData = baseData >> 6;

					dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());

					//hasLockDown
					if (hasLockDownEnd) {
						decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
						//baseObjectTmp.setLockDown(decryptResult.getResult());
						dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());
					}

					//hasProtectionEnd
					if (hasProtectionEnd) {
						decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
						baseObjectTmp.setProtectionEnd(decryptResult.getResult());
						baseObjectTmp.setHasProtectionEnd(hasProtectionEnd);
						dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());
					}

					//hasProtectionEnd
					if (hasSupportAlert) {
						decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
						//baseObjectTmp.setSupportAlertStart(decryptResult.getResult());
						dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());

						decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
						//baseObjectTmp.setSupportAlertEnd(decryptResult.getResult());
						dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());
					}

					//hasMoveCoolDown
					if (hasMoveCoolDownEndStep) {
						decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
						//baseObjectTmp.setMoveCoolDownEndStep(decryptResult.getResult());
						dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());
					}

					//hasMoveRestriction
					if (hasMoveRestrictionEndStep) {
						decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
						//baseObjectTmp.setMoveRestrictionEndStep(decryptResult.getResult());
						dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());

						decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
						//baseObjectTmp.setMoveRestrictionCoord(decryptResult.getResult());
						dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());
					}

					//hasRecoveryEndStep
					if (hasRecoveryEndStep) {
						decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
						//baseObjectTmp.setRecoveryEndStep(decryptResult.getResult());
						dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());
					}

					//Building condition
					decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
					baseObjectTmp.setConditionBuilding(decryptResult.getResult());
					dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());

					//Defense condition
					if (hasConditionDefense) {
						decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
						baseObjectTmp.setConditionDefense(decryptResult.getResult());
						dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());
					}


					//Unknown
					decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
					dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());

					//Base ID
					decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
					baseObjectTmp.setBaseId(decryptResult.getResult());
					dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());

					//Unknown
					decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 5);
					dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());


					//Base Name
					baseObjectTmp.setName(dataMapStr);

					log.debug("Adding Base object {}", baseObjectTmp);

					//We can add it to the list
					listeMapObject.add(baseObjectTmp);
					break;

				case 4 : 
					//POI
					Poi poiObjectTmp = new Poi();

					poiObjectTmp.setCoords(coords);

					//POI Level
					decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 4);
					int dataTmp = decryptResult.getResult();
					poiObjectTmp.setLevel(dataTmp & 255);
					dataTmp = dataTmp >> 8;

					//POI type
					int idTypePoi = dataTmp & 7;
					poiObjectTmp.setType(idTypePoi);

					dataTmp = dataTmp >> 3;

					//Alliance index
					int indexAlliance = dataTmp & 1023;
					if (indexAlliance > 0 && alliancesList.containsKey(indexAlliance)) {
						poiObjectTmp.setAllianceId(alliancesList.get(indexAlliance).getAllianceId());
					}

					dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());

					log.debug("Adding POI {}", poiObjectTmp);

					//Add to the list
					listeMapObject.add(poiObjectTmp);
					break;
				default:
					continue;
				}


			}
		}
		return listeMapObject;
	}


	/**
	 * Process EndGame
	 * @param ENDGAME poll string
	 * @return
	 */
	public List<EndGame> processPollEndGame(List<String> ch) {
		List<EndGame> listEndgame = new ArrayList<EndGame>();
		for (String dataMapStr : ch) {
			if (!StringUtils.isEmpty(dataMapStr)) {
				//Map Object
				EndGame endGameTmp = new EndGame();

				//Non utilisé
				DecryptResult decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 4);
				dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());
				decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 4);
				dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());


				decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 4);
				int coordData = decryptResult.getResult();
				int coordX = coordData & 4095;
				coordData = coordData >> 13;
				int coordY = coordData;

				dataMapStr = dataMapStr.substring(decryptResult.getCurrentIndex(), dataMapStr.length());

				decryptResult = CryptoUtils.base91Decode(dataMapStr, 0, 2);
				int typeData = decryptResult.getResult();
				int type = typeData & 3;

				switch(type){
				case 1:
					coordX+=3;
					coordY+=3;
					break;
				case 2:
					endGameTmp.setStep(typeData >> 2);
					break;
				case 3:
					coordX+=2;
					coordY+=2;
					break;
				}
				endGameTmp.setType(type);

				Coords coords = new Coords(coordX, coordY);
				endGameTmp.setCoords(coords);
				listEndgame.add(endGameTmp);
			}

		}
		return listEndgame;
	}

}
