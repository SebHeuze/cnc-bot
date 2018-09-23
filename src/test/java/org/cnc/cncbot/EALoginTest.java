package org.cnc.cncbot;


import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.cnc.cncbot.dto.OriginAccountInfo;
import org.cnc.cncbot.dto.ResponseType;
import org.cnc.cncbot.service.retrofit.AccountsEAService;
import org.cnc.cncbot.service.retrofit.GameCDNOriginService;
import org.cnc.cncbot.service.retrofit.ServiceGenerator;
import org.cnc.cncbot.service.retrofit.SigninEAService;
import org.cnc.cncbot.service.retrofit.TiberiumAlliancesService;
import org.cnc.cncbot.utils.HttpUtils;
import org.junit.Ignore;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

@Slf4j
public class EALoginTest {

	@Test
	@Ignore
	public void connectAuthTest() throws IOException {
		AccountsEAService accountsEaService = ServiceGenerator.createService(AccountsEAService.class, AccountsEAService.BASE_URL, ResponseType.PLAIN_TEXT);
		SigninEAService signinEaService = ServiceGenerator.createService(SigninEAService.class, SigninEAService.BASE_URL, ResponseType.PLAIN_TEXT);
		TiberiumAlliancesService tiberiumAlliancesService = ServiceGenerator.createService(TiberiumAlliancesService.class, TiberiumAlliancesService.BASE_URL, ResponseType.PLAIN_TEXT);
		GameCDNOriginService gameCDNService = ServiceGenerator.createService(GameCDNOriginService.class, GameCDNOriginService.BASE_URL, ResponseType.JSON);

		
		Call<Void> call0 = tiberiumAlliancesService.loginAuth();
		Response<Void> response0 = call0.execute();
		
		
		log.info("Taille header " + response0.headers().values("Set-Cookie"));
		String JessionIDTiberium = response0.headers().values("Set-Cookie").stream().filter(it -> it.contains("JSESSIONID")).collect(Collectors.toList()).get(0);
		log.info("Cookie JSESSIONID" + JessionIDTiberium);
		log.info(response0.headers().get("Set-Cookie"));
		log.info(response0.headers().get("Location"));
		URL uri0 = new URL(response0.headers().get("Location"));
		String state = HttpUtils.queryToMap(uri0.getQuery()).get("state");

		log.info("Code retour 0 {}", response0.code());
		
		Call<String> call1 = accountsEaService.connectAuth("ccta-web-server", "https://www.tiberiumalliances.com/login_check","fr_FR","code",state);
		Response<String> response1 = call1.execute();
		log.info(response1.headers().get("Location"));
		log.info("Code retour 1 {}", response1.code());
		
		URL uri = new URL(response1.headers().get("Location"));
		
		Call<String> call2 = signinEaService.login(HttpUtils.queryToMap(uri.getQuery()).get("fid"));
		Response<String> response2 = call2.execute();
		log.info(response2.headers().get("Location"));
		log.info(response2.headers().get("Set-Cookie"));
		log.info("Code retour 2 {}", response2.code());
		

		URL uri2 = new URL(SigninEAService.BASE_URL + response2.headers().get("Location"));
		/*Call<Void> call3 = signinEaService.login(
				response2.headers().get("Set-Cookie"),
				HttpUtils.splitQuery(uri2).get("execution"),
				URLDecoder.decode(HttpUtils.splitQuery(uri2).get("initref"), "UTF-8"));
		Response<Void> response3 = call3.execute();
		log.info("Code retour 3 {}", response3.code());
		log.info(response3.headers().get("Location"));*/
		
		Call<Void> call4 = signinEaService.login(
				response2.headers().get("Set-Cookie"),
				HttpUtils.queryToMap(uri2.getQuery()).get("execution"),
				URLDecoder.decode(HttpUtils.queryToMap(uri2.getQuery()).get("initref"), "UTF-8"),
				"user","password","FR",null,null,"on","submit",null,"false",null);
		Response<Void> response4 = call4.execute();
		log.info("Code retour 4 {}", response4.code());
		log.info(response4.headers().get("Location"));
		

		URL uri3 = new URL(SigninEAService.BASE_URL + response4.headers().get("Location"));
		/*Call<Void> call5 = signinEaService.login(
				response2.headers().get("Set-Cookie"),
				HttpUtils.splitQuery(uri3).get("execution"),
				URLDecoder.decode(HttpUtils.splitQuery(uri3).get("initref"), "UTF-8"));
		Response<Void> response5 = call5.execute();
		log.info("Code retour 5 {}", response5.code());
		log.info(response5.headers().get("Location"));*/
		

		Call<Void> call6 = signinEaService.login(
				response2.headers().get("Set-Cookie"),
				HttpUtils.queryToMap(uri3.getQuery()).get("execution"),
				URLDecoder.decode(HttpUtils.queryToMap(uri3.getQuery()).get("initref"), "UTF-8"),
				"end");
		Response<Void> response6 = call6.execute();
		log.info("Code retour 6 {}", response6.code());
		log.info(response6.headers().get("Location"));
	        
		URL uri4 = new URL(response6.headers().get("Location"));
		Call<String> call7 = accountsEaService.connectAuth("ccta-web-server", "https://www.tiberiumalliances.com/login_check","fr_FR","code",state,
				HttpUtils.queryToMap(uri4.getQuery()).get("fid"));
		Response<String> response7 = call7.execute();
		log.info(response7.headers().get("Location"));
		log.info("Code retour 7 {}", response7.code());
		
		

		URL uri5 = new URL(response7.headers().get("Location"));
		Call<String> call8 = tiberiumAlliancesService.loginCheck(
				JessionIDTiberium ,HttpUtils.queryToMap(uri5.getQuery()).get("code"),state);
		Response<String> response8 = call8.execute();
		log.info(response8.headers().get("Location"));
		log.info("Code retour 8 {}", response8.code());
		
		Call<String> call9 = tiberiumAlliancesService.gameLaunch(
				response8.headers().get("Set-Cookie"));
		Response<String> response9 = call9.execute();
		log.info(response9.headers().get("Location"));
		log.info("Body {}", response9.body());
		
		Pattern pattern = Pattern.compile("name=\"sessionId\" value=\"([^\"]*)\"", Pattern.MULTILINE);
	    Matcher matcher = pattern.matcher(response9.body());
	    if (!matcher.find()) {
	    	fail("sessionId non trouvée");
	    }
	    log.info(matcher.group(1));
		
	    Map<String,Object> params = new HashMap<>();
	    params.put("session", matcher.group(1));
	    Call<OriginAccountInfo> originAccountCall  = gameCDNService.getOriginAccountInfo(params);
		Response<OriginAccountInfo> originAccountResponse = originAccountCall.execute();

		log.info("Code retour 9 {}", originAccountResponse.code());
	}

}
