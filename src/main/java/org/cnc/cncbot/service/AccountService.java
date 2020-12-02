package org.cnc.cncbot.service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.cnc.cncbot.dto.OriginAccountInfo;
import org.cnc.cncbot.dto.ResponseType;
import org.cnc.cncbot.dto.UserSession;
import org.cnc.cncbot.exception.AuthException;
import org.cnc.cncbot.exception.EAAuthException;
import org.cnc.cncbot.service.retrofit.AccountsEAService;
import org.cnc.cncbot.service.retrofit.EAService;
import org.cnc.cncbot.service.retrofit.GameCDNOriginService;
import org.cnc.cncbot.service.retrofit.ServiceGenerator;
import org.cnc.cncbot.service.retrofit.SigninEAService;
import org.cnc.cncbot.service.retrofit.TiberiumAlliancesService;
import org.cnc.cncbot.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Account service
 * Used to manage TiberiumAlliance/EA sessions
 * @author SebHeuze
 *
 */
@Service
@Setter
@Slf4j
public class AccountService {

	/**
	 * Retrofit services class
	 */
	public final AccountsEAService accountsEaService;
	public final SigninEAService signinEaService;
	public final EAService eaService;
	public final GameCDNOriginService gameCDNService;

	/**
	 * Account EA sessionsId storage
	 */
	public Map<String, String> loggedAccounts = new HashMap<>();


	/**
	 * Call Params names
	 */
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
	public final static String OAUTH_CLIENT_ID = "EADOTCOM-WEB-SERVER";
	public final static String OAUTH_REDIRECT_URI = "https://www.ea.com/login_check";
	public final static String OAUTH_LOCALE = "fr_FR";
	public final static String OAUTH_RESPONSE_TYPE = "code";

	public final static String SESSION_ID_REGEX = "name=\"sessionId\" value=\"([^\"]*)\"";

	@Autowired
	public AccountService() {
		this.accountsEaService = ServiceGenerator.createService(AccountsEAService.class, AccountsEAService.BASE_URL, ResponseType.PLAIN_TEXT);
		this.signinEaService = ServiceGenerator.createService(SigninEAService.class, SigninEAService.BASE_URL, ResponseType.PLAIN_TEXT);
		this.eaService = ServiceGenerator.createService(EAService.class, EAService.BASE_URL, ResponseType.PLAIN_TEXT);
		this.gameCDNService = ServiceGenerator.createService(GameCDNOriginService.class, GameCDNOriginService.BASE_URL, ResponseType.JSON);
	}

	/**
	 * Return if an account is currently logged
	 * @param userSession
	 * @return boolean true if logged in
	 */
	public boolean isLogged(UserSession userSession) {
		return this.loggedAccounts.containsKey(userSession.getUser());
	}

	/**
	 * Logout account
	 * @param userSession
	 */
	public void logout(UserSession userSession) {
		this.loggedAccounts.remove(userSession.getUser());
	}

	/**
	 * Return Session Id of account
	 * @param userSession
	 * @return String sessionId
	 */
	public String getSessionId(UserSession userSession) {
		return this.loggedAccounts.get(userSession.getUser());
	}

	/**
	 * Connect account with EA oauth system
	 * @param account
	 */
	public void connect(UserSession userSession) throws AuthException {

		log.info("Connect to account {} on world {}", userSession.getUser(), userSession.getWorldId());


		try {
			/*
			 * Initial Call to get Tiberium Alliance Cookies and state param
			 */
			Call<Void> initialAuthCall = this.eaService.launch();
			Response<Void> initialAuthResponse = initialAuthCall.execute();

			log.info("Set-Cookie Header call 1/6 " + initialAuthResponse.headers().values(HEADER_SET_COOKIE));

			String playSessionIDTiberium = initialAuthResponse.headers().values("Set-Cookie").stream().filter(it -> it.contains("PLAY_SESSION")).collect(Collectors.toList()).get(0);

			if (StringUtils.isEmpty(playSessionIDTiberium)) {
				throw new AuthException("Can't retrieve PLAY_SESSION on auth call 1");
			}

			URL redirectUri = new URL(initialAuthResponse.headers().get(HEADER_LOCATION));

			String state = HttpUtils.queryToMap(redirectUri.getQuery()).get(STATE_PARAM);
			if (state == null) {
				throw new AuthException("Can't retrieve state param on auth call 1");
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
				throw new AuthException("Can't retrieve fid param on auth call 2");
			}


			/*
			 * First Login Call
			 */
			Call<String> firstLoginCall = this.signinEaService.login(fid);
			Response<String> firstLoginResponse = firstLoginCall.execute();
			String eaCookies = String.join("; ", firstLoginResponse.headers().toMultimap().get("Set-Cookie"));
			log.info(firstLoginResponse.headers().get(HEADER_LOCATION));
			log.info(eaCookies);


			redirectUri = new URL(SigninEAService.BASE_URL + firstLoginResponse.headers().get(HEADER_LOCATION));


			if (StringUtils.isEmpty(eaCookies)) {
				throw new AuthException("Can't retrieve Set-Cookie on auth call 3");
			}

			String execution = HttpUtils.queryToMap(redirectUri.getQuery()).get(EXECUTION_PARAM);
			if (execution == null) {
				throw new AuthException("Can't retrieve execution param on auth call 3");
			}

			String initrefEncoded = HttpUtils.queryToMap(redirectUri.getQuery()).get(INITREF_PARAM);
			if (initrefEncoded == null) {
				throw new AuthException("Can't retrieve initref param on auth call 3");
			}

			String initref = URLDecoder.decode(initrefEncoded, "UTF-8");

			/*
			 * Second Login Call
			 */

			Call<Void> secondLoginCall = this.signinEaService.login(
					eaCookies,
					execution,
					initref);
			Response<Void> secondLoginResponse = secondLoginCall.execute();
			log.info(secondLoginResponse.headers().get(HEADER_LOCATION));
			
			Call<Void> secondLoginCallBis = this.signinEaService.login(
					eaCookies,
					execution,
					initref,
					userSession.getUser(),userSession.getPassword(),"FR",null,null,"on", "on","submit",null,"false",null);
			Response<Void> secondLoginBisResponse = secondLoginCallBis.execute();
			log.info(secondLoginBisResponse.headers().get(HEADER_LOCATION));


			redirectUri = new URL(SigninEAService.BASE_URL + secondLoginBisResponse.headers().get(HEADER_LOCATION));

			execution = HttpUtils.queryToMap(redirectUri.getQuery()).get(EXECUTION_PARAM);
			if (execution == null) {
				throw new AuthException("Can't retrieve execution param on auth call 4");
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
				throw new AuthException("Can't retrieve code param on auth call 6");
			}

			/*
			 * Login check
			 */
			Call<String> loginCheckCall = this.eaService.loginCheck(
					playSessionIDTiberium ,code,state);
			Response<String> loginCheckResponse = loginCheckCall.execute();

			Call<String> connectAuthExpireCall = accountsEaService.connectAuthExpire(String.join("; ", secondAuthResponse.headers().toMultimap().get("Set-Cookie")), "ccta-web-server-game", "https://gamecdnorigin.alliances.commandandconquer.com/Farm/service.svc/ajaxEndpoint/ssoconsume","3599","code","0", "", "fr");
			Response<String> connectAuthExpireResponse = connectAuthExpireCall.execute();
				
			URL connectAuthExpireUrl = new URL(connectAuthExpireResponse.headers().get("Location"));
			Call<String> call9 = gameCDNService.ssoConsume("loginRedirectInternal=1",HttpUtils.queryToMap(connectAuthExpireUrl.getQuery()).get("code"),"0");
			Response<String> response9 = call9.execute();
			
			Pattern pattern = Pattern.compile("sessionId=([^;]*);", Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(response9.headers().get("Set-Cookie"));

			if (!matcher.find()) {
				throw new AuthException("Can't get sessionId with regex");
			} else {
				log.info("Session found");
			}

			this.loggedAccounts.put(userSession.getUser(), matcher.group(1));

		} catch (SocketTimeoutException ste) {
			log.error("Timeout during authentification of account {}", userSession.getUser(), ste);
			throw new AuthException("Timeout during EA authentification");
		} catch (IOException ioe) {
			log.error("Error during authentification of account {}", userSession.getUser(), ioe);
			throw new EAAuthException("Error during EA authentification");
		}
	}


	/**
	 * Get OriginAccountInfo
	 * @param userSession
	 */
	public OriginAccountInfo getAccountInfo(UserSession userSession) {

		try {
			Map<String,Object> params = new HashMap<>();
			params.put("session", userSession.getSessionId());
			Call<OriginAccountInfo> originAccountCall  = this.gameCDNService.getOriginAccountInfo(params);
			Response<OriginAccountInfo> originAccountResponse = originAccountCall.execute();
			OriginAccountInfo result = originAccountResponse.body();
			if (result.getServers() == null) {
				log.error(originAccountResponse.toString());
				this.logout(userSession);
				throw new AuthException("Error while getting account info on account " + userSession.getUser() + " World " + userSession.getWorldId());
			}
			return result;
		} catch (IOException ioe) {
			log.error("Error during refreshAccountInfo of account {}", userSession.getUser(), ioe);
			throw new AuthException("Error during refreshAccountInfo of account " + userSession.getUser() );
		}
		
	}
	

}
