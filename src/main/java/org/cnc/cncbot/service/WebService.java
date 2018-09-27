package org.cnc.cncbot.service;

import org.cnc.cncbot.config.DBContext;
import org.cnc.cncbot.dto.UserSession;
import org.cnc.cncbot.dto.sendmessage.Message;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.map.dao.AccountDAO;
import org.cnc.cncbot.map.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class WebService {

	public final GameService gameService;

	public final AccountDAO accountDAO;

	@Autowired
	public WebService(GameService gameService, AccountDAO accountDAO) {
		this.gameService = gameService;
		this.accountDAO = accountDAO;
	}

	public void sendMessage(Message unMessage) {
		log.info("Sending message {}", unMessage);

		DBContext.setDatasource("cncmap");
		
		Account account = this.accountDAO.findByWorldIdAndActiveTrue(unMessage.getMonde());
		if (account == null){
			throw new BatchException("No account for world "+ unMessage.getMonde());
		}
	
		UserSession userSession = new UserSession(account.getUser(), account.getPass(), account.getWorldId(), 0, 0,
				null, "World42Dummy", null);
		String gameSessionId = this.gameService.launchWorld(userSession);
		userSession.setGameSessionId(gameSessionId);
		
		this.gameService.sendMessage(unMessage, userSession);
	}

}
