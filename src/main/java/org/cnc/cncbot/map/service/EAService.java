package org.cnc.cncbot.map.service;

import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.map.entities.Account;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * EA services class
 * @author sheuze
 *
 */
@Service
@Setter
@Slf4j
public class EAService {
	private static final String BASE_URL = "https://accounts.ea.com/";
	 
	https://accounts.ea.com/connect/auth?client_id=ccta-web-server&redirect_uri=https%3A%2F%2Fwww.tiberiumalliances.com%2Flogin_check&locale=fr_FR&response_type=code&state=test
		
   
    
	public final void login(Account account) throws BatchException {
		
	}
}
