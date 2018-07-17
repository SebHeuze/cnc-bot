package org.cnc.cncbot.map.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.cnc.cncbot.dto.generated.OriginAccountInfo;
import org.cnc.cncbot.dto.generated.PollWorld;
import org.cnc.cncbot.dto.generated.S;
import org.cnc.cncbot.dto.generated.Server;
import org.cnc.cncbot.dto.serverinfos.ServerInfoResponse;
import org.cnc.cncbot.exception.AuthException;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.map.dto.MapData;
import org.cnc.cncbot.map.dto.MapObject;
import org.cnc.cncbot.map.entities.Account;
import org.cnc.cncbot.map.entities.Alliance;
import org.cnc.cncbot.map.entities.Joueur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

	public final AccountService accountService;

	public final GameService gameService;

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

	@Autowired
	public MapService(AccountService accountService, GameService gameService) {
		this.accountService = accountService;
		this.gameService = gameService;
	}

	/**
	 * Main method for Map Batch
	 * @param batchNumber
	 * @throws BatchException
	 */
	public void mapDataJob(int batchNumber) throws BatchException {

		List<Account> accountList = this.accountService.getAccountsForBatch(batchNumber);

		for (Account account : accountList) {
			try {
				this.launchWorld(account);
			} catch (AuthException ae){
				log.error("Error during auth step with account {}", account.getUser(), ae);
			}
		}

	}

	/**
	 * Launch world and get Game Session Id
	 * @param account
	 */
	public void launchWorld(Account account) {
		if (!this.accountService.isLogged(account)) {
			this.accountService.connect(account);
		}
		OriginAccountInfo accountInfos = this.accountService.getServerInfos(account);
		Optional<Server> server = accountInfos.getServers()
				.stream()
				.filter(item -> item.getId().equals(account.getMonde()))
				.collect(Collectors.reducing((a, b) -> null));
		if (!server.get().getOnline()) {
			throw new BatchException("World offline " + server.get().getId() + " User " + account.getUser());
		}
		this.gameService.init(server.get());
		String gameSessionId = this.accountService.getServerInfos(account).getSessionGUID();

		this.gameService.openGameSession(account, gameSessionId);

		ServerInfoResponse serverInfo = this.gameService.getServerInfos(gameSessionId);
	}


	@Async
	public CompletableFuture<MapData> getMapDataTile(int x , ServerInfoResponse serverInfos, String gameSessionId) throws InterruptedException {
		//Objets Data
		List<MapObject> listeObject = new ArrayList<MapObject>();
		Map<Integer, Alliance> listeAlliances = new HashMap<Integer, Alliance>();
		Map<Integer, Joueur> listeJoueurs = new HashMap<Integer, Joueur>();
		Set<Alliance> listeAlliancesTotal = new HashSet<Alliance>();
		Set<Joueur> listeJoueursTotal = new HashSet<Joueur>();

		//Parser
		JsonParser parser = new JsonParser();

		String retourJson = this.gameService.poll(x, (int) (serverInfos.getWw() / SIZE_TILE) + 1, gameSessionId);

		JsonArray jsonArray = parser.parse(retourJson).getAsJsonArray();
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
				listeObject.addAll(batchService.traiterPollEndGame(pollRequestEndGame.getCH()));
				log.debug(pollRequestEndGame.toString());
			}
		}

		if (pollRequest !=  null) {
			for (S sectorData : pollRequest.getS()) {
				listeAlliances.clear();
				listeJoueurs.clear();

				int secteurY = sectorData.getI() >> 8;
				int secteurX = sectorData.getI() & 255;
				log.debug("Analyse du secteur X : {} Y : {}", secteurX, secteurY);

				listeAlliances  =  batchService.traiterPollAlliances(sectorData.getA());
				listeAlliancesTotal.addAll(listeAlliances.values());
				listeJoueurs = batchService.traiterPollJoueurs(sectorData.getP(), listeAlliances);
				listeJoueursTotal.addAll(listeJoueurs.values());
				listeObject.addAll(batchService.traiterPollMapObject(sectorData.getD(), listeAlliances, listeJoueurs, secteurX, secteurY));

			}

		} else {
			throw new BatchException("Erreur lors de la récupération des données WORLD du Batch Poll");
		}

		return CompletableFuture.completedFuture(new MapData(listeObject, listeAlliancesTotal, listeJoueursTotal));
	}
}
