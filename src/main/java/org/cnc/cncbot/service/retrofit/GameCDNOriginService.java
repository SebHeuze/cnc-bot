package org.cnc.cncbot.service.retrofit;

import java.util.Map;

import org.cnc.cncbot.dto.generated.OriginAccountInfo;
import org.springframework.stereotype.Service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Game CDN Origin services class
 * @author sheuze
 *
 */
@Service
public interface GameCDNOriginService {
	static final String BASE_URL = "https://gamecdnorigin.alliances.commandandconquer.com/Farm/Service.svc/ajaxEndpoint/";
	 

	@POST("GetOriginAccountInfo")
	Call<OriginAccountInfo> getOriginAccountInfo(@Body Map<String,Object> body);
	
}
