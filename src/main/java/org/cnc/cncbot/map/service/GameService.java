package org.cnc.cncbot.map.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.cnc.cncbot.dto.ResponseType;
import org.cnc.cncbot.dto.generated.OriginAccountInfo;
import org.cnc.cncbot.dto.generated.Server;
import org.cnc.cncbot.dto.opensession.OpenSessionRequest;
import org.cnc.cncbot.dto.opensession.OpenSessionResponse;
import org.cnc.cncbot.dto.poll.PollRequest;
import org.cnc.cncbot.dto.sendmessage.Message;
import org.cnc.cncbot.dto.sendmessage.SendMessageRequest;
import org.cnc.cncbot.dto.serverinfos.ServerInfoRequest;
import org.cnc.cncbot.dto.serverinfos.ServerInfoResponse;
import org.cnc.cncbot.exception.AuthException;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.exception.GameException;
import org.cnc.cncbot.map.dto.UserSession;
import org.cnc.cncbot.map.entities.Account;
import org.cnc.cncbot.map.service.retrofit.CNCGameService;
import org.cnc.cncbot.map.service.retrofit.ServiceGenerator;
import org.cnc.cncbot.map.utils.CncUtils;
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
	public String launchWorld(Account account) {
		return this.launchWorld(account, 0);
	}

	/**
	 * Launch world and get Game Session Id
	 * @param account
	 * @param retryCount nb of login retry
	 * @return game session Id
	 */
	public String launchWorld(Account account, int retryCount) {
		if (!this.accountService.isLogged(account)) {
			this.accountService.connect(account);
		}
		OriginAccountInfo accountInfos = this.accountService.getOriginAccountInfo(account);
		Optional<Server> server = accountInfos.getServers()
				.stream()
				.filter(item -> item.getId().equals(account.getMonde()))
				.collect(Collectors.reducing((a, b) -> null));
		if (!server.get().getOnline()) {
			throw new BatchException("World offline " + server.get().getId() + " User " + account.getUser());
		}
		this.init(server.get());

		String gameSessionId = this.openGameSession(account, accountInfos.getSessionGUID());

		if (gameSessionId.equals(EXPIRED_GAME_SESSIONID)) {
			log.info("Session expirée pour le compte {}", account.getUser());
			if (retryCount >= MAX_RETRY) {
				throw new AuthException("Can't log on account " + account.getUser() + " World " + account.getMonde());
			}
			this.accountService.logout(account);
			return this.launchWorld(account, ++retryCount);
		}

		return gameSessionId;
	}
	
	
	/**
	 * Get  server infos
	 * @param sessionId
	 * @return
	 */
	public ServerInfoResponse getServerInfos(String gameSessionId) {

		try {
			Call<ServerInfoResponse> getServerInfoCall  = this.cncGameService.getServerInfo(
					ServerInfoRequest.builder().session(gameSessionId).build());
			return getServerInfoCall.execute().body();
		} catch (IOException e) {
			log.error("Error with request getServerInfos", e);
			throw new GameException("Error with request getServerInfos");
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
	public String openGameSession(Account account, String sessionId) {

		try {
			Call<OpenSessionResponse> openGameSessionCall  = this.cncGameService.openSession(
					OpenSessionRequest.builder().session(sessionId).build());
			
			return openGameSessionCall.execute().body().getI();
		} catch (IOException e) {
			log.error("Error opening game session of account {}", account.getUser(), e);
			throw new GameException("Error opening game session of account " + account.getUser());
		}
	}

}
