package org.cnc.cncbot.map.service;

import java.util.List;

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
	
	@Autowired
	public MapService(AccountService accountService) {
		this.accountService = accountService;
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
				this.accountService.connect(account);
			} catch (AuthException ae){
				log.error("Error during auth step with account {}", account.getUser(), ae);
			}
		}
		
	}
}
