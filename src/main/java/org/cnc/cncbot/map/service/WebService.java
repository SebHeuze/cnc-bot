package org.cnc.cncbot.map.service;

import org.cnc.cncbot.dto.sendmessage.Message;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.map.dto.UserSession;
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

	public final AccountService accountService;

	public final GameService gameService;


	@Autowired
	public WebService(AccountService accountService, GameService gameService) {
		this.accountService = accountService;
		this.gameService = gameService;
	}

	public void sendMessage(Message unMessage) {
		log.info("Sending message {}", unMessage);
		Account account = this.accountService.getAccount(unMessage.getMonde());
		if (account == null){
			throw new BatchException("No account for world "+ unMessage.getMonde());
		}
		
		if (!this.accountService.isLogged(account)) {
			this.accountService.connect(account);
		}

		String gameSessionId = this.gameService.launchWorld(account);
		UserSession userSession = new UserSession(0, 0, gameSessionId, "World42Dummy", this.accountService.getOriginAccountInfo(account).getSessionGUID());

		this.gameService.sendMessage(unMessage, userSession);
	}

}
