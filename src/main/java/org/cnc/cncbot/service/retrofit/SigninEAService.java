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
public interface SigninEAService {
	static final String BASE_URL = "https://signin.ea.com/";
	 
   
	@GET("p/web2/login")
	Call<String> login(@Query("fid") String fid);
	
	

	@GET("p/web2/login")
	Call<Void> login( @Header("Cookie") String cookie,
						@Query("execution") String execution,
						@Query("initref") String initRef,
						@Query("_eventId") String eventId);
	
	@GET("p/web2/login")
	Call<Void> login( @Header("Cookie") String cookie,
						@Query("execution") String execution,
						@Query("initref") String initRef);
	@FormUrlEncoded
	@POST("p/web2/login")
	Call<Void> login( @Header("Cookie") String cookie,
						@Query("execution") String execution,
						@Query("initref") String initRef,
						@Field("email") String email,
						@Field("password") String password,
						@Field("country") String country,
						@Field("phoneNumber") String phoneNumber,
						@Field("passwordForPhone") String passwordForPhone,
						@Field("_rememberMe") String _rememberMe,
						@Field("rememberMe") String rememberMe,
						@Field("_eventId") String eventId,
						@Field("gCaptchaResponse") String gCaptchaResponse,
						@Field("isPhoneNumberLogin") String isPhoneNumberLogin,
						@Field("isIncompletePhone") String isIncompletePhone);

}
