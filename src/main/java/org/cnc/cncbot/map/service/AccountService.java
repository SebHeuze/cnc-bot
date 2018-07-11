package org.cnc.cncbot.map.service;

import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.ListUtils;
import org.cnc.cncbot.exception.AuthException;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.map.dao.AccountDAO;
import org.cnc.cncbot.map.entities.Account;
import org.cnc.cncbot.map.service.retrofit.AccountsEAService;
import org.cnc.cncbot.map.service.retrofit.ServiceGenerator;
import org.cnc.cncbot.map.service.retrofit.SigninEAService;
import org.cnc.cncbot.map.service.retrofit.TiberiumAlliancesService;
import org.cnc.cncbot.map.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Query;

/**
 * Map service
 * @author sheuze
 *
 */
@Service
@Setter
@Slf4j
public class AccountService {
	
	public final AccountDAO accountDAO;
	
	public final AccountsEAService accountsEaService;
	
	public final SigninEAService signinEaService;
	
	public final TiberiumAlliancesService tiberiumAlliancesService;
		
	public final static String STATE_PARAM = "state";
	
	/**
	 * Header names
	 */
	public final static String HEADER_SET_COOKIE = "Set-Cookie";
	public final static String HEADER_LOCATION = "Location";
	
	/**
	 * OAUTH Config
	 */
	public final static String OAUTH_CLIENT_ID = "ccta-web-server";
	public final static String OAUTH_REDIRECT_URI = "https://www.tiberiumalliances.com/login_check";
	public final static String OAUTH_LOCALE = "fr_FR";
	public final static String OAUTH_RESPONSE_TYPE = "code";
	
	
	@Autowired
	public AccountService(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
		
		this.accountsEaService = ServiceGenerator.createService(AccountsEAService.class, AccountsEAService.BASE_URL);
		this.signinEaService = ServiceGenerator.createService(SigninEAService.class, SigninEAService.BASE_URL);
		this.tiberiumAlliancesService = ServiceGenerator.createService(TiberiumAlliancesService.class, TiberiumAlliancesService.BASE_URL);
	}
	public List<Account> getAccountsForBatch(int batchNumber) {
		
		List<Account> accountList = this.accountDAO.findByNumbatch(batchNumber);
		log.info("accounts retrieved : {}", accountList.size());
		
		return accountList;
	}
	
	public void connect(Account account) {
		
		log.info("Connect to account {} on world {}", account.getUser(), account.getMonde());
		
		
		/*
		 * Initial Call to get Tiberium Alliance Cookies and state param
		 */
		Call<Void> initialAuthCall = this.tiberiumAlliancesService.loginAuth();
		Response<Void> initialAuthResponse = initialAuthCall.execute();
		
		log.info("Set-Cookie Header Initial Call " + initialAuthResponse.headers().values(HEADER_SET_COOKIE));
		log.debug("Http Code Initial Call {}", initialAuthResponse.code());
		
		String tiberiumAllianceCookies = initialAuthResponse.headers().get(HEADER_SET_COOKIE);
		
		if (StringUtils.isEmpty(tiberiumAllianceCookies)) {
			throw new AuthException("Can't retrieve Set-Cookie on initial auth call");
		}
		
		URL redirectUri = new URL(initialAuthResponse.headers().get(HEADER_LOCATION));
		List<String> stateGetValues = HttpUtils.splitQuery(redirectUri).get(STATE_PARAM);
		
		if (stateGetValues == null || stateGetValues.size() == 0) {
			throw new AuthException("Can't retrieve state param on initial auth call");
		}
		
		String state = stateGetValues.get(0);
		
		
		/*
		 * First Auth Call
		 */
		Call<String> call1 = this.accountsEaService.connectAuth(
				OAUTH_CLIENT_ID,
				OAUTH_REDIRECT_URI,
				OAUTH_LOCALE,
				OAUTH_RESPONSE_TYPE,
				state);
		Response<String> response1 = call1.execute();
		log.info(response1.headers().get(HEADER_LOCATION));
		log.info("Code retour 1 {}", response1.code());
		
		URL uri = new URL(response1.headers().get(HEADER_LOCATION));
		
		Call<String> call2 = this.signinEaService.login(HttpUtils.splitQuery(uri).get("fid").get(0));
		Response<String> response2 = call2.execute();
		log.info(response2.headers().get(HEADER_LOCATION));
		log.info(response2.headers().get(HEADER_SET_COOKIE));
		log.info("Code retour 2 {}", response2.code());
		

		URL uri2 = new URL(SigninEAService.BASE_URL + response2.headers().get(HEADER_LOCATION));
		Call<Void> call3 = this.signinEaService.login(
				response2.headers().get(HEADER_SET_COOKIE),
				HttpUtils.splitQuery(uri2).get("execution").get(0),
				URLDecoder.decode(HttpUtils.splitQuery(uri2).get("initref").get(0), "UTF-8"));
		Response<Void> response3 = call3.execute();
		log.info("Code retour 3 {}", response3.code());
		log.info(response3.headers().get(HEADER_LOCATION));
		
		Call<Void> call4 = this.signinEaService.login(
				response2.headers().get(HEADER_SET_COOKIE),
				HttpUtils.splitQuery(uri2).get("execution").get(0),
				URLDecoder.decode(HttpUtils.splitQuery(uri2).get("initref").get(0), "UTF-8"),
				"qzd","qzd","FR",null,null,"on","submit",null,"false",null);
		Response<Void> response4 = call4.execute();
		log.info("Code retour 4 {}", response4.code());
		log.info(response4.headers().get(HEADER_LOCATION));
		

		URL uri3 = new URL(SigninEAService.BASE_URL + response4.headers().get(HEADER_LOCATION));
		Call<Void> call5 = this.signinEaService.login(
				response2.headers().get(HEADER_SET_COOKIE),
				HttpUtils.splitQuery(uri3).get("execution").get(0),
				URLDecoder.decode(HttpUtils.splitQuery(uri3).get("initref").get(0), "UTF-8"));
		Response<Void> response5 = call5.execute();
		log.info("Code retour 5 {}", response5.code());
		log.info(response5.headers().get(HEADER_LOCATION));
		

		Call<Void> call6 = this.signinEaService.login(
				response2.headers().get(HEADER_SET_COOKIE),
				HttpUtils.splitQuery(uri3).get("execution").get(0),
				URLDecoder.decode(HttpUtils.splitQuery(uri3).get("initref").get(0), "UTF-8"),
				"end");
		Response<Void> response6 = call6.execute();
		log.info("Code retour 6 {}", response6.code());
		log.info(response6.headers().get(HEADER_LOCATION));
	        
		URL uri4 = new URL(response6.headers().get(HEADER_LOCATION));
		Call<String> call7 = this.accountsEaService.connectAuth(OAUTH_CLIENT_ID, "https://www.tiberiumalliances.com/login_check","fr_FR","code",state,
				HttpUtils.splitQuery(uri4).get("fid").get(0));
		Response<String> response7 = call7.execute();
		log.info(response7.headers().get(HEADER_LOCATION));
		log.info("Code retour 7 {}", response7.code());
		
		

		URL uri5 = new URL(response7.headers().get(HEADER_LOCATION));
		Call<String> call8 = this.tiberiumAlliancesService.loginCheck(
				JessionIDTiberium ,HttpUtils.splitQuery(uri5).get("code").get(0),state);
		Response<String> response8 = call8.execute();
		log.info(response8.headers().get(HEADER_LOCATION));
		log.info("Code retour 8 {}", response8.code());
		
		Call<String> call9 = this.tiberiumAlliancesService.gameLaunch(
				response8.headers().get(HEADER_SET_COOKIE));
		Response<String> response9 = call9.execute();
		log.info(response9.headers().get(HEADER_LOCATION));
		log.info("Body {}", response9.body());
	}
	
}
