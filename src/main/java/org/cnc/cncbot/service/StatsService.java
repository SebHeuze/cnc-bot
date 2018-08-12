package org.cnc.cncbot.service;

import java.util.Date;
import java.util.List;

import org.cnc.cncbot.config.DBContext;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.stats.dao.AccountDAO;
import org.cnc.cncbot.stats.dao.BatchLogDAO;
import org.cnc.cncbot.stats.entities.Account;
import org.cnc.cncbot.stats.entities.BatchLog;
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
public class StatsService {

	public final AccountService accountService;
	public final GameService gameService;

	public final AccountDAO accountDAO;
	public final BatchLogDAO batchLogDAO;
	
	@Autowired
	public StatsService(AccountService accountService, GameService gameService, AccountDAO accountDAO, BatchLogDAO batchLogDAO) {
		this.accountService = accountService;
		this.gameService = gameService;
		this.accountDAO = accountDAO;
		this.batchLogDAO = batchLogDAO;
	}

	/**
	 * Main method for Stats Batch
	 * @throws BatchException
	 */
	public void statsJob() throws BatchException {
		DBContext.setDatasource("cctastats");
		
		log.info("Launch of stats batch");
		
	    List<Account> accountList = this.accountDAO.findAll();
	    
	    BatchLog batchLog = new BatchLog();
	    batchLog.setDateDebut(new Date());
	    batchLog.setNbWorlds(accountList.size());
	    
	    batchLog = this.batchLogDAO.save(batchLog);
	    
		log.info("Accounts size {}", accountList.size());
		
		

	}

}
