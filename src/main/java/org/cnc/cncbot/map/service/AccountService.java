package org.cnc.cncbot.map.service;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

import org.cnc.cncbot.exception.AuthException;
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
	public final static String FID_PARAM = "fid";
	public final static String INITREF_PARAM = "initref";
	public final static String EXECUTION_PARAM = "execution";
	public final static String CODE_PARAM = "code";
	
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
		
		
		try {
		/*
		 * Initial Call to get Tiberium Alliance Cookies and state param
		 */
		Call<Void> initialAuthCall = this.tiberiumAlliancesService.loginAuth();
		Response<Void> initialAuthResponse = initialAuthCall.execute();
		
		log.info("Set-Cookie Header Initial Call " + initialAuthResponse.headers().values(HEADER_SET_COOKIE));
		
		String JessionIDTiberium = initialAuthResponse.headers().values("Set-Cookie").stream().filter(it -> it.contains("JSESSIONID")).collect(Collectors.toList()).get(0);
		
		if (StringUtils.isEmpty(JessionIDTiberium)) {
			throw new AuthException("Can't retrieve JSESSIONID on initial auth call");
		}
		
		URL redirectUri = new URL(initialAuthResponse.headers().get(HEADER_LOCATION));
		
		String state = HttpUtils.queryToMap(redirectUri.getQuery()).get(STATE_PARAM);
		if (state == null) {
			throw new AuthException("Can't retrieve state param on initial auth call");
		}
		
		
		
		/*
		 * First Auth Call
		 */
		Call<String> firstAuthCall = this.accountsEaService.connectAuth(
				OAUTH_CLIENT_ID,
				OAUTH_REDIRECT_URI,
				OAUTH_LOCALE,
				OAUTH_RESPONSE_TYPE,
				state);
		Response<String> firstAuthResponse = firstAuthCall.execute();
		log.info("First auth call done, redirecting to {}",firstAuthResponse.headers().get(HEADER_LOCATION));
		
		redirectUri = new URL(firstAuthResponse.headers().get(HEADER_LOCATION));
		
		String fid = HttpUtils.queryToMap(redirectUri.getQuery()).get(FID_PARAM);
		if (fid == null) {
			throw new AuthException("Can't retrieve fid param on first auth call");
		}
		
		
		/*
		 * First Login Call
		 */
		Call<String> firstLoginCall = this.signinEaService.login(fid);
		Response<String> firstLoginResponse = firstLoginCall.execute();
		log.info(firstLoginResponse.headers().get(HEADER_LOCATION));
		log.info(firstLoginResponse.headers().get(HEADER_SET_COOKIE));
		

		redirectUri = new URL(SigninEAService.BASE_URL + firstLoginResponse.headers().get(HEADER_LOCATION));
		
		
		String eaCookies = firstLoginResponse.headers().get(HEADER_SET_COOKIE);
		if (StringUtils.isEmpty(eaCookies)) {
			throw new AuthException("Can't retrieve Set-Cookie on first EA Login call");
		}
		
		String execution = HttpUtils.queryToMap(redirectUri.getQuery()).get(EXECUTION_PARAM);
		if (execution == null) {
			throw new AuthException("Can't retrieve execution param on first login call");
		}
		
		String initrefEncoded = HttpUtils.queryToMap(redirectUri.getQuery()).get(INITREF_PARAM);
		if (initrefEncoded == null) {
			throw new AuthException("Can't retrieve initref param on first login call");
		}
		
		String initref = URLDecoder.decode(initrefEncoded, "UTF-8");
		
		/*
		 * Second Login Call
		 */
		Call<Void> secondLoginCall = this.signinEaService.login(
				eaCookies,
				execution,
				initref,
				account.getUser(),account.getPass(),"FR",null,null,"on","submit",null,"false",null);
		Response<Void> secondLoginResponse = secondLoginCall.execute();
		log.info(secondLoginResponse.headers().get(HEADER_LOCATION));
		

		redirectUri = new URL(SigninEAService.BASE_URL + secondLoginResponse.headers().get(HEADER_LOCATION));
	
		execution = HttpUtils.queryToMap(redirectUri.getQuery()).get(EXECUTION_PARAM);
		if (execution == null) {
			throw new AuthException("Can't retrieve execution param on second login call");
		}
		
		
		/*
		 * Third Login Call
		 */
		Call<Void> thirdLoginCall = this.signinEaService.login(
				eaCookies,
				execution,
				initref,
				"end");
		Response<Void> thirdLoginResponse = thirdLoginCall.execute();
		log.info(thirdLoginResponse.headers().get(HEADER_LOCATION));
	        

		redirectUri = new URL(thirdLoginResponse.headers().get(HEADER_LOCATION));
		
		
		/*
		 * Second Auth Call
		 */
		Call<String> secondAuthCall = this.accountsEaService.connectAuth(
				OAUTH_CLIENT_ID,
				OAUTH_REDIRECT_URI,
				OAUTH_LOCALE,
				OAUTH_RESPONSE_TYPE,
				state,
				fid);
		Response<String> secondAuthResponse = secondAuthCall.execute();
		log.info(secondAuthResponse.headers().get(HEADER_LOCATION));
		
		

		redirectUri = new URL(secondAuthResponse.headers().get(HEADER_LOCATION));
		
		String code = HttpUtils.queryToMap(redirectUri.getQuery()).get(CODE_PARAM);
		if (code == null) {
			throw new AuthException("Can't retrieve code param on second auth call");
		}
		
		/*
		 * Login check
		 */
		Call<String> loginCheckCall = this.tiberiumAlliancesService.loginCheck(
				JessionIDTiberium ,code,state);
		Response<String> loginCheckResponse = loginCheckCall.execute();
		
		Call<String> call9 = this.tiberiumAlliancesService.gameLaunch(
				loginCheckResponse.headers().get(HEADER_SET_COOKIE));
		Response<String> response9 = call9.execute();
		log.info("Body {}", response9.body());

		
		} catch (IOException ioe) {
			log.error("Erreur lors du login du compte {}", account.getUser(), ioe);
		}
	}
	
}
