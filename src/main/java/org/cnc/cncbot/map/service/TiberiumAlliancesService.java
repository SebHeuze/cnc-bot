package org.cnc.cncbot.map.service;

import org.springframework.stereotype.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * EA services class
 * @author sheuze
 *
 */
@Service
public interface TiberiumAlliancesService {
	static final String BASE_URL = "https://www.tiberiumalliances.com/";
	 

	@GET("fr/login/auth")
	Call<Void> loginAuth();
	
	@GET("fr/game/launch")
	Call<String> gameLaunch(@Header("Cookie") String cookie);
	
	@GET("login_check")
	Call<String> loginCheck(@Header("Cookie") String cookie,
			                @Query("code") String code,
			                @Query("state") String state);
	
}
