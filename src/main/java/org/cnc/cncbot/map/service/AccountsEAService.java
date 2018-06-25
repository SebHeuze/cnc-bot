package org.cnc.cncbot.map.service;

import org.springframework.stereotype.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * EA services class
 * @author sheuze
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
}
