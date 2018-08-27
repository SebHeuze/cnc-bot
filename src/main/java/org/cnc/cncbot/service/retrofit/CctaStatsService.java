package org.cnc.cncbot.service.retrofit;

import org.springframework.stereotype.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Tiberium Alliances services class
 * @author sheuze
 *
 */
@Service
public interface CctaStatsService {
	static final String BASE_URL = "http://www.ccta-stats.com/";
	 

	@GET("${org.cnc.cncbot.cctastats.getregisterdplayers.url}")
	Call<Void> getRegisteredPlayersList();
	
	
}
