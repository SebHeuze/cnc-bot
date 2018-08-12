package org.cnc.cncbot.map.service;

import org.cnc.cncbot.config.DBContext;
import org.cnc.cncbot.dto.sendmessage.Message;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.map.dao.AccountDAO;
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

	public final AccountDAO accountDAO;

	@Autowired
	public WebService(AccountService accountService, GameService gameService, AccountDAO accountDAO) {
		this.accountService = accountService;
		this.gameService = gameService;
		this.accountDAO = accountDAO;
	}

	public void sendMessage(Message unMessage) {
		log.info("Sending message {}", unMessage);

		DBContext.setDatasource("cncmap");
		
		Account account = this.accountDAO.findByMondeAndActiveTrue(unMessage.getMonde());
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
