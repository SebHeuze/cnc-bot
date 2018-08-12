package org.cnc.cncbot.map.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.cnc.cncbot.config.DBContext;
import org.cnc.cncbot.dto.generated.OriginAccountInfo;
import org.cnc.cncbot.dto.generated.PollWorld;
import org.cnc.cncbot.dto.generated.S;
import org.cnc.cncbot.dto.generated.Server;
import org.cnc.cncbot.dto.serverinfos.ServerInfoResponse;
import org.cnc.cncbot.exception.AuthException;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.map.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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


	@Autowired
	public StatsService(AccountService accountService, GameService gameService) {
		this.accountService = accountService;
		this.gameService = gameService;
	}

	/**
	 * Main method for Stats Batch
	 * @throws BatchException
	 */
	public void statsJob() throws BatchException {
		DBContext.setDatasource("cctastats");
		
		log.info("Launch of stats batch");
		
	    List<Account> accountList = this.accountService.getAccount();

	    long idBatch = this.scriptingDAO.createBatchLog(listeComptes.size());
	    
		log.info("Account size {}",this.accountService.getAccountsForBatch(32).size());

	}

}
