package org.cnc.cncbot.service.retrofit;

import org.springframework.stereotype.Service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * EA services class
 * @author SebHeuze
 *
 */
@Service
public interface EAService {
	static final String BASE_URL = "https://www.ea.com/";
   
	@GET("page/ccta/launch?homePageUrl=/fr-fr/games/command-and-conquer/command-and-conquer-tiberium-alliances")
	Call<Void> launch();
	
	@GET("login_check")
	Call<String> loginCheck(@Header("Cookie") String cookie,
			                @Query("code") String code,
			                @Query("state") String state);
}
