package org.cnc.cncbot.map.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cnc.cncbot.dto.ResponseType;
import org.cnc.cncbot.dto.generated.OpenSessionResponse;
import org.cnc.cncbot.dto.generated.OriginAccountInfo;
import org.cnc.cncbot.dto.generated.Server;
import org.cnc.cncbot.exception.GameException;
import org.cnc.cncbot.map.entities.Account;
import org.cnc.cncbot.map.service.retrofit.CNCGameService;
import org.cnc.cncbot.map.service.retrofit.ServiceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

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
	
	
	public void openGameSession(Account account, String sessionId) {

		try {
		    Map<String,Object> params = new HashMap<>();
		    params.put("platformId", 1);
		    params.put("refId", -1);
		    params.put("reset", true);
		    params.put("session", sessionId);
		    params.put("version", -1);
		    Call<OpenSessionResponse> openGameSessionCall  = this.cncGameService.openSession(params);
			Response<OpenSessionResponse> openGameSessionResponse = openGameSessionCall.execute();
			log.info("Game id ");
		} catch (IOException e) {
			log.error("Error opening game session of account {}", account.getUser(), e);
			throw new GameException("Error opening game session of account " + account.getUser());
		}
	}
	
	
}
