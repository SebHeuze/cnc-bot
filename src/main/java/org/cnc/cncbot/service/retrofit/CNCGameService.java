package org.cnc.cncbot.service.retrofit;

import java.util.ArrayList;

import org.cnc.cncbot.dto.opensession.OpenSessionRequest;
import org.cnc.cncbot.dto.opensession.OpenSessionResponse;
import org.cnc.cncbot.dto.poll.PollRequest;
import org.cnc.cncbot.dto.publicallianceinfo.PublicAllianceInfoRequest;
import org.cnc.cncbot.dto.publicallianceinfo.PublicAllianceInfoResponse;
import org.cnc.cncbot.dto.publicplayerinfo.PublicPlayerInfoRequest;
import org.cnc.cncbot.dto.publicplayerinfo.PublicPlayerInfoResponse;
import org.cnc.cncbot.dto.rankingcount.RankingCountRequest;
import org.cnc.cncbot.dto.rankingdata.RankingDataRequest;
import org.cnc.cncbot.dto.rankingdata.RankingDataResponse;
import org.cnc.cncbot.dto.sendmessage.SendMessageRequest;
import org.cnc.cncbot.dto.serverinfos.ServerInfoRequest;
import org.cnc.cncbot.dto.serverinfos.ServerInfoResponse;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Game CDN Origin services class
 * @author SebHeuze
 *
 */
@Service
public interface CNCGameService {

	@GET("index.aspx")
	Call<String> indexPage(@Header("Cookie") String cookie);
	
	@POST("/Presentation/Service.svc/ajaxEndpoint/OpenSession")
	Call<OpenSessionResponse> openSession(@Body OpenSessionRequest openSessionRequest);
	
	@POST("/Presentation/Service.svc/ajaxEndpoint/Poll")
	Call<JsonArray> poll(@Body PollRequest pollRequest);	
	
	@POST("/Presentation/Service.svc/ajaxEndpoint/GetServerInfo")
	Call<ServerInfoResponse> getServerInfo(@Body ServerInfoRequest getServerInfoRequest);

	@POST("/Presentation/Service.svc/ajaxEndpoint/IGMBulkSendMsg")
	Call<ArrayList<Integer>> sendMessage(@Body SendMessageRequest sendMessageRequest);

	@POST("/Presentation/Service.svc/ajaxEndpoint/RankingGetCount")
	Call<Integer> rankingGetCount(@Body RankingCountRequest rankingGetCountRequest);
	
	@POST("/Presentation/Service.svc/ajaxEndpoint/RankingGetData")
	Call<RankingDataResponse> rankingGetData(@Body RankingDataRequest rankingGetDataRequest);
	
	@POST("/Presentation/Service.svc/ajaxEndpoint/GetPublicPlayerInfo")
	Call<PublicPlayerInfoResponse> getPublicPlayerInfo(@Body PublicPlayerInfoRequest getPublicPlayerInfoRequest);
	
	@POST("/Presentation/Service.svc/ajaxEndpoint/GetPublicAllianceInfo")
	Call<PublicAllianceInfoResponse> getPublicAllianceInfo(@Body PublicAllianceInfoRequest getPublicAllianceInfoRequest);
	
}
