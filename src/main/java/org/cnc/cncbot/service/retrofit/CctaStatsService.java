package org.cnc.cncbot.service.retrofit;

import java.util.List;

import org.cnc.cncbot.dto.cctastats.JoueursRegistered;
import org.springframework.stereotype.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Tiberium Alliances services class
 * @author sheuze
 *
 */
@Service
public interface CctaStatsService {
	static final String BASE_URL = "http://www.ccta-stats.com/";
	 
	@GET("index.php?a=getJoueurs")
	Call<List<JoueursRegistered>> getPlayersRegistered(@Query("monde") int idMonde);
	

	@GET("index.php?a=cacheclear")
	Call<List<JoueursRegistered>> clearCache(@Query("monde") int idMonde);
}
