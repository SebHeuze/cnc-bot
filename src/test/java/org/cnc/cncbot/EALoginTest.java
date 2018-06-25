package org.cnc.cncbot;


import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import org.cnc.cncbot.map.service.AccountsEAService;
import org.cnc.cncbot.map.service.ServiceGenerator;
import org.cnc.cncbot.map.service.SigninEAService;
import org.cnc.cncbot.map.service.TiberiumAlliancesService;
import org.cnc.cncbot.map.utils.HttpUtils;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

@Slf4j
public class EALoginTest {

	@Test
	public void connectAuthTest() throws IOException {
		AccountsEAService accountsEaService = ServiceGenerator.createService(AccountsEAService.class, AccountsEAService.BASE_URL);
		SigninEAService signinEaService = ServiceGenerator.createService(SigninEAService.class, SigninEAService.BASE_URL);
		TiberiumAlliancesService tiberiumAlliancesService = ServiceGenerator.createService(TiberiumAlliancesService.class, TiberiumAlliancesService.BASE_URL);

		Call<String> call = accountsEaService.connectAuth("ccta-web-server", "https://www.tiberiumalliances.com/login_check","fr_FR","code","test");
		Response<String> response = call.execute();
		log.info(response.headers().get("Location"));
		log.info("Code retour {}", response.code());
		
		URL uri = new URL(response.headers().get("Location"));
		
		Call<String> call2 = signinEaService.login(HttpUtils.splitQuery(uri).get("fid").get(0));
		Response<String> response2 = call2.execute();
		log.info(response2.headers().get("Location"));
		log.info(response2.headers().get("Set-Cookie"));
		log.info("Code retour {}", response2.code());
		

		URL uri2 = new URL(SigninEAService.BASE_URL + response2.headers().get("Location"));
		Call<Void> call3 = signinEaService.login(
				response2.headers().get("Set-Cookie"),
				HttpUtils.splitQuery(uri2).get("execution").get(0),
				URLDecoder.decode(HttpUtils.splitQuery(uri2).get("initref").get(0), "UTF-8"));
		Response<Void> response3 = call3.execute();
		log.info("Code retour {}", response3.code());
		log.info(response3.headers().get("Location"));
		
		Call<Void> call4 = signinEaService.login(
				response2.headers().get("Set-Cookie"),
				HttpUtils.splitQuery(uri2).get("execution").get(0),
				URLDecoder.decode(HttpUtils.splitQuery(uri2).get("initref").get(0), "UTF-8"),
				"test@test.fr","pass","FR",null,null,"on","submit",null,"false",null);
		Response<Void> response4 = call4.execute();
		log.info("Code retour {}", response4.code());
		log.info(response4.headers().get("Location"));
		

		URL uri3 = new URL(SigninEAService.BASE_URL + response4.headers().get("Location"));
		Call<Void> call5 = signinEaService.login(
				response2.headers().get("Set-Cookie"),
				HttpUtils.splitQuery(uri3).get("execution").get(0),
				URLDecoder.decode(HttpUtils.splitQuery(uri3).get("initref").get(0), "UTF-8"));
		Response<Void> response5 = call5.execute();
		log.info("Code retour {}", response5.code());
		log.info(response5.headers().get("Location"));
		

		Call<Void> call6 = signinEaService.login(
				response2.headers().get("Set-Cookie"),
				HttpUtils.splitQuery(uri3).get("execution").get(0),
				URLDecoder.decode(HttpUtils.splitQuery(uri3).get("initref").get(0), "UTF-8"),
				"end");
		Response<Void> response6 = call6.execute();
		log.info("Code retour {}", response6.code());
		log.info(response6.headers().get("Location"));
	        
		URL uri4 = new URL(response6.headers().get("Location"));
		Call<String> call7 = accountsEaService.connectAuth("ccta-web-server", "https://www.tiberiumalliances.com/login_check","fr_FR","code","test",
				HttpUtils.splitQuery(uri4).get("fid").get(0));
		Response<String> response7 = call7.execute();
		log.info(response7.headers().get("Location"));
		log.info("Code retour {}", response7.code());
		
		
		Call<String> call8 = tiberiumAlliancesService.gameLaunch(response2.headers().get("Set-Cookie"));
		Response<String> response8 = call8.execute();
		log.info("Body {}", response8.body());
	}

}
