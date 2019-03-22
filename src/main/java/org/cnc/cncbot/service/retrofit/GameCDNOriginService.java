package org.cnc.cncbot.service.retrofit;

import java.util.Map;

import org.cnc.cncbot.dto.OriginAccountInfo;
import org.springframework.stereotype.Service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Game CDN Origin services class
 * @author SebHeuze
 *
 */
@Service
public interface GameCDNOriginService {
	static final String BASE_URL = "https://gamecdnorigin.alliances.commandandconquer.com/Farm/Service.svc/ajaxEndpoint/";
	 

	@POST("GetOriginAccountInfo")
	Call<OriginAccountInfo> getOriginAccountInfo(@Body Map<String,Object> body);
	
	@GET("ssoconsume")
	Call<String> ssoConsume(@Header("Cookie") String cookie,
			                @Query("code") String code,
			                @Query("state") String state);
	
}
