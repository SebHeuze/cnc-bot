package org.cnc.cncbot.service.retrofit;

import org.springframework.stereotype.Service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * EA services class
 * @author SebHeuze
 *
 */
@Service
public interface SigninEAService {
	static final String BASE_URL = "https://signin.ea.com/";
	 
   
	@GET("p/juno/login")
	Call<String> login(@Query("fid") String fid);
	
	

	@GET("p/juno/login")
	Call<Void> login( @Header("Cookie") String cookie,
						@Query("execution") String execution,
						@Query("initref") String initRef,
						@Query("_eventId") String eventId);
	
	@GET("p/juno/login")
	Call<String> login( @Header("Cookie") String cookie,
						@Query("execution") String execution,
						@Query("initref") String initRef);
	@FormUrlEncoded
	@Headers({
        "Content-Type: application/x-www-form-urlencoded"
	})
	@POST("p/juno/login")
	Call<Void> login( @Header("Cookie") String cookie,
						@Query("execution") String execution,
						@Query("initref") String initRef,
						@Field("email") String email,
						@Field("regionCode") String regionCode,
						@Field("phoneNumber") String phoneNumber,
						@Field("password") String password,
						@Field("_eventId") String eventId,
						@Field("cid") String cid,
						@Field("showAgeUp") String showAgeUp,
						@Field("thirdPartyCaptchaResponse") String gCaptchaResponse,
						@Field("loginMethod") String loginMethod,
						@Field("_rememberMe") String _rememberMe);

}
