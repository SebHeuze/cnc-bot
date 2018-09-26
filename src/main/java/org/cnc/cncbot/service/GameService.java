package org.cnc.cncbot.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.cnc.cncbot.dto.OriginAccountInfo;
import org.cnc.cncbot.dto.ResponseType;
import org.cnc.cncbot.dto.Server;
import org.cnc.cncbot.dto.UserSession;
import org.cnc.cncbot.dto.opensession.OpenSessionRequest;
import org.cnc.cncbot.dto.opensession.OpenSessionResponse;
import org.cnc.cncbot.dto.poll.PollRequest;
import org.cnc.cncbot.dto.publicallianceinfo.PublicAllianceInfoRequest;
import org.cnc.cncbot.dto.publicallianceinfo.PublicAllianceInfoResponse;
import org.cnc.cncbot.dto.publicplayerinfo.PublicPlayerInfoRequest;
import org.cnc.cncbot.dto.publicplayerinfo.PublicPlayerInfoResponse;
import org.cnc.cncbot.dto.rankingcount.RankingCountRequest;
import org.cnc.cncbot.dto.rankingdata.RankingDataRequest;
import org.cnc.cncbot.dto.rankingdata.RankingDataResponse;
import org.cnc.cncbot.dto.sendmessage.Message;
import org.cnc.cncbot.dto.sendmessage.SendMessageRequest;
import org.cnc.cncbot.dto.serverinfos.ServerInfoRequest;
import org.cnc.cncbot.dto.serverinfos.ServerInfoResponse;
import org.cnc.cncbot.exception.AuthException;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.exception.GameException;
import org.cnc.cncbot.service.retrofit.CNCGameService;
import org.cnc.cncbot.service.retrofit.ServiceGenerator;
import org.cnc.cncbot.utils.CncUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;

/**
 * Account service
 * Used to manage TiberiumAlliance/EA sessions
 * @author sheuze
 *
 */
@Service
@Setter
@Slf4j
@Scope("prototype")
public class GameService {

	public CNCGameService cncGameService;

	public final AccountService accountService;

	public final static String URL_PATH_API = "/Presentation/Service.svc/ajaxEndpoint/";

	/**
	 * Expired game session id
	 */
	public static final String EXPIRED_GAME_SESSIONID = "00000000-0000-0000-0000-000000000000";

	/**
	 * Max RETRY for Auth
	 */
	public static final int MAX_RETRY = 3;


	@Autowired
	public GameService(AccountService accountService) {
		this.accountService = accountService;
	}

	public void init(Server server) {
		this.cncGameService = ServiceGenerator.createService(CNCGameService.class, server.getUrl() + URL_PATH_API, ResponseType.JSON);
	}

	/**
	 * Launch world and get Game Session Id
	 * @param account
	 * @return game session Id
	 */
	public String launchWorld(UserSession userSession) {
		return this.launchWorld(userSession, 0);
	}

	/**
	 * Launch world and get Game Session Id
	 * @param account
	 * @param retryCount nb of login retry
	 * @return game session Id
	 */
	public String launchWorld(UserSession userSession, int retryCount) {
		if (!this.accountService.isLogged(userSession)) {
			this.accountService.connect(userSession);
		} 
		userSession.setSessionId(this.accountService.getSessionId(userSession));

		OriginAccountInfo accountInfos = this.accountService.getAccountInfo(userSession);
		Optional<Server> server = accountInfos.getServers()
				.stream()
				.filter(item -> item.getId().equals(userSession.getWorldId()))
				.collect(Collectors.reducing((a, b) -> null));
		if (!server.get().getOnline()) {
			throw new BatchException("World offline " + server.get().getId() + " User " + userSession.getUser());
		}
		this.init(server.get());

		String gameSessionId = this.openGameSession(userSession);

		if (gameSessionId.equals(EXPIRED_GAME_SESSIONID)) {
			log.info("Session expirée pour le compte {}", userSession.getUser());
			if (retryCount >= MAX_RETRY) {
				throw new AuthException("Can't log on account " + userSession.getUser() + " World " + userSession.getWorldId());
			}
			this.accountService.logout(userSession);
			return this.launchWorld(userSession, ++retryCount);
		}

		return gameSessionId;
	}


	/**
	 * Get  server infos
	 * @param sessionId
	 * @return
	 */
	public ServerInfoResponse getServerInfos(UserSession userSession) {

		try {
			Call<ServerInfoResponse> getServerInfoCall  = this.cncGameService.getServerInfo(
					ServerInfoRequest.builder().session(userSession.getGameSessionId()).build());
			return getServerInfoCall.execute().body();
		} catch (IOException e) {
			log.error("Error with request getServerInfos", e);
			throw new GameException("Error with request getServerInfos");
		}
	}

	/**
	 * Ranking Get Count Request.
	 * @param view (Alliance = 1, player = 0)
	 * @param rankingType Score = 0...
	 * @return count
	 */
	public int getRankingCount(UserSession userSession, int view, int rankingType) {

		try {
			Call<Integer> getRankingCountCall  = this.cncGameService.rankingGetCount(
					RankingCountRequest.builder().rankingType(0).view(0).session(userSession.getGameSessionId()).build());
			return getRankingCountCall.execute().body();
		} catch (IOException e) {
			log.error("Error with request getRankingCount", e);
			throw new GameException("Error with request getRankingCount");
		}
	}
	
	/**
     * Récupérer les informations joueurs.
     * @param id l'id du joueur
     * @return requete
     */
	PublicPlayerInfoResponse getPublicPlayerInfoRequest(UserSession userSession, int id) {
		try {
			log.debug("getPublicPlayerInfoRequest id {}", id);
			Call<PublicPlayerInfoResponse> getRankingCountCall  = this.cncGameService.getPublicPlayerInfo(
					PublicPlayerInfoRequest.builder().id(id).session(userSession.getGameSessionId())
					.build());
			return getRankingCountCall.execute().body();
		} catch (IOException e) {
			log.error("Error with request getPublicPlayerInfoRequest", e);
			throw new GameException("Error with request getPublicPlayerInfoRequest");
		}
	}
	
	/**
     * Récupérer les informations joueurs.
     * @param id l'id du joueur
     * @return requete
     */
	PublicAllianceInfoResponse getPublicAllianceInfoRequest(UserSession userSession, int id) {
		try {
			Call<PublicAllianceInfoResponse> getPublicAllianceInfoCall  = this.cncGameService.getPublicAllianceInfo(
					PublicAllianceInfoRequest.builder().id(id).session(userSession.getGameSessionId())
					.build());
			return getPublicAllianceInfoCall.execute().body();
		} catch (IOException e) {
			log.error("Error with request getPublicAllianceInfoRequest", e);
			throw new GameException("Error with request getPublicAllianceInfoRequest");
		}
	}
	
	/**
	 * Ranking Get Data Request.
	 * @param view (Alliance = 1, Joueur = 0)
	 * @param rankingType Score = 0...
	 * @param firstIndex début index
	 * @param lastIndex fin index
	 * @param sortColumn Par quelle colonne ordonner les joueurs (score = 2)
	 * @param ascending true ou false
	 * @return requete
	 */
	public RankingDataResponse getRankingData(UserSession userSession, int view,
			int firstIndex, int lastIndex, int sortColumn, boolean ascending) {
		try {
			Call<RankingDataResponse> getRankingCountCall  = this.cncGameService.rankingGetData(
					RankingDataRequest.builder().view(view)
					.firstIndex(firstIndex)
					.lastIndex(lastIndex)
					.sortColumn(sortColumn)
					.ascending(ascending)
					.session(userSession.getGameSessionId())
					.build());
			return getRankingCountCall.execute().body();
		} catch (IOException e) {
			log.error("Error with request getRankingCount", e);
			throw new GameException("Error with request getRankingCount");
		}
	}


	/**
	 * Poll
	 * @param sessionId
	 * @return
	 */
	public JsonArray poll(int mapX, int maxY, UserSession userSession) {

		try {
			Call<JsonArray> pollCall  = this.cncGameService.poll(
					PollRequest.builder()
					.session(userSession.getGameSessionId())
					.requests(CncUtils.buildPollRequest(mapX,maxY))
					.sequenceid(userSession.useSequenceId())
					.requestid(userSession.useRequestId()).build());
			return pollCall.execute().body();
		} catch (IOException e) {
			log.error("Error with request poll", e);
			throw new GameException("Error with request poll");
		}
	}

	/**
	 * send message
	 * @param sessionId
	 * @return
	 */
	public boolean sendMessage(Message unMessage, UserSession userSession) {

		try {
			Call<ArrayList<Integer>> pollCall  = this.cncGameService.sendMessage(
					SendMessageRequest.builder()
					.session(userSession.getGameSessionId())
					.body("<cnc><cncs>"+userSession.getPlayerName()+"</cncs><cncd>1408406611796</cncd><cnct>" + unMessage.getMessage() + "</cnct></cnc>")
					.players(unMessage.getPseudo())
					.subject(unMessage.getTitre())
					.build());
			ArrayList<Integer> result = pollCall.execute().body();
			if (result.size() != 2 || !result.get(0).equals(1) || !result.get(1).equals(1)){
				throw new BatchException("Echec lors de l'envoi du message : " + result);
			}
			return true;
		} catch (IOException e) {
			log.error("Error with request sendMessage", e);
			throw new GameException("Error with request sendMessage");
		}
	}


	/**
	 * Open game session to get Game Session Id
	 * @param account
	 * @param sessionId
	 */
	public String openGameSession(UserSession userSession) {

		try {
			Call<OpenSessionResponse> openGameSessionCall  = this.cncGameService.openSession(
					OpenSessionRequest.builder().session(userSession.getSessionId()).build());

			return openGameSessionCall.execute().body().getI();
		} catch (IOException e) {
			log.error("Error opening game session of account {}", userSession.getUser(), e);
			throw new GameException("Error opening game session of account " + userSession.getUser());
		}
	}

}
