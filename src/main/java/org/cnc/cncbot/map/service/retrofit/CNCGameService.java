package org.cnc.cncbot.map.service.retrofit;

import java.util.Map;

import org.cnc.cncbot.dto.generated.OpenSessionResponse;
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
public interface CNCGameService {
	
	@POST("OpenSession")
	Call<OpenSessionResponse> openSession(@Body Map<String,Object> body);
	
}
