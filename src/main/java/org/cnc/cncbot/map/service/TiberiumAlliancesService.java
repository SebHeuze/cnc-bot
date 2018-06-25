package org.cnc.cncbot.map.service;

import org.springframework.stereotype.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * EA services class
 * @author sheuze
 *
 */
@Service
public interface TiberiumAlliancesService {
	static final String BASE_URL = "https://www.tiberiumalliances.com/";
	 
   
	@GET("fr/game/launch")
	Call<String> gameLaunch(@Header("Cookie") String cookie);
	
}
