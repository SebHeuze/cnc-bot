package org.cnc.cncbot.service.retrofit;

import org.springframework.stereotype.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * EA services class
 * @author SebHeuze
 *
 */
@Service
public interface AccountsEAService {
	static final String BASE_URL = "https://accounts.ea.com/";
	 
   
	@GET("connect/auth")
	Call<String> connectAuth(@Query("client_id") String clientId,
							@Query("redirect_uri") String redirectUri,
							@Query("locale") String locale,
							@Query("response_type") String responseType,
							@Query("state") String state);
	

	@GET("connect/auth")
	Call<String> connectAuth(@Query("client_id") String clientId,
							@Query("redirect_uri") String redirectUri,
							@Query("locale") String locale,
							@Query("response_type") String responseType,
							@Query("state") String state,
							@Query("fid") String fid);
	

	@GET("connect/auth")
	Call<String> connectAuthExpire( @Header("Cookie") String cookie,
							@Query("client_id") String clientId,
							@Query("redirect_uri") String redirectUri,
							@Query("expires_in") String expireIn,
							@Query("response_type") String responseType,
							@Query("state") String state);
}
