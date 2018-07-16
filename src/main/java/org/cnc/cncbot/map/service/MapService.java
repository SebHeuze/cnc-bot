package org.cnc.cncbot.map.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.cnc.cncbot.dto.generated.OriginAccountInfo;
import org.cnc.cncbot.dto.generated.Server;
import org.cnc.cncbot.exception.AuthException;
import org.cnc.cncbot.exception.BatchException;
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
public class MapService {
	
	public final AccountService accountService;
	
	public final GameService gameService;
	
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
		Optional<Server> serverInfos = accountInfos.getServers()
								.stream()
								.filter(item -> item.getId().equals(account.getMonde()))
								.collect(Collectors.reducing((a, b) -> null));
		if (!serverInfos.get().getOnline()) {
			//TODO : StringFormat
			throw new BatchException("World offline " + serverInfos.get().getId() + " User " + account.getUser());
		}
		this.gameService.init(serverInfos.get());
		this.gameService.openGameSession(account,this.accountService.getServerInfos(account).getSessionGUID());
	}
	
}
