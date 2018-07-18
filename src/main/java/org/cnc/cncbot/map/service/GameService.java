package org.cnc.cncbot.map.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cnc.cncbot.dto.ResponseType;
import org.cnc.cncbot.dto.generated.Server;
import org.cnc.cncbot.dto.opensession.OpenSessionRequest;
import org.cnc.cncbot.dto.opensession.OpenSessionResponse;
import org.cnc.cncbot.dto.poll.PollRequest;
import org.cnc.cncbot.dto.serverinfos.ServerInfoRequest;
import org.cnc.cncbot.dto.serverinfos.ServerInfoResponse;
import org.cnc.cncbot.exception.GameException;
import org.cnc.cncbot.map.entities.Account;
import org.cnc.cncbot.map.service.retrofit.CNCGameService;
import org.cnc.cncbot.map.service.retrofit.ServiceGenerator;
import org.cnc.cncbot.map.utils.CncUtils;
import org.springframework.stereotype.Service;

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
public class GameService {

	public CNCGameService cncGameService;

	public final static String URL_PATH_API = "/Presentation/Service.svc/ajaxEndpoint/";

	public void init(Server server) {
		this.cncGameService = ServiceGenerator.createService(CNCGameService.class, server.getUrl() + URL_PATH_API, ResponseType.JSON);
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
	 * Get  server infos
	 * @param sessionId
	 * @return
	 */
	public String poll(int mapX, int maxY, String gameSessionId) {

		try {
			Call<String> pollCall  = this.cncGameService.poll(
					PollRequest.builder().session(gameSessionId).request(CncUtils.buildPollRequest(mapX,maxY)).build());
			return pollCall.execute().body();
		} catch (IOException e) {
			log.error("Error with request getServerInfos", e);
			throw new GameException("Error with request getServerInfos");
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
