package org.cnc.cncbot.service.retrofit;

import java.util.ArrayList;

import org.cnc.cncbot.dto.opensession.OpenSessionRequest;
import org.cnc.cncbot.dto.opensession.OpenSessionResponse;
import org.cnc.cncbot.dto.poll.PollRequest;
import org.cnc.cncbot.dto.sendmessage.SendMessageRequest;
import org.cnc.cncbot.dto.serverinfos.ServerInfoRequest;
import org.cnc.cncbot.dto.serverinfos.ServerInfoResponse;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;

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
	Call<OpenSessionResponse> openSession(@Body OpenSessionRequest openSessionRequest);
	
	@POST("Poll")
	Call<JsonArray> poll(@Body PollRequest pollRequest);	
	
	@POST("GetServerInfo")
	Call<ServerInfoResponse> getServerInfo(@Body ServerInfoRequest getServerInfoRequest);

	@POST("IGMBulkSendMsg")
	Call<ArrayList<Integer>> sendMessage(@Body SendMessageRequest sendMessageRequest);

	/*
	@POST("RankingGetCount")
	Call<RankingGetCountResponse> rankingGetCount(@Body RankingGetCountRequest rankingGetCountRequest);
	
	@POST("RankingGetData")
	Call<RankingGetDataResponse> rankingGetData(@Body RankingGetDataRequest rankingGetDataRequest);
	
	@POST("GetPublicPlayerInfo")
	Call<getPublicPlayerInfoResponse> getPublicPlayerInfo(@Body GetPublicPlayerInfoRequest getPublicPlayerInfoRequest);
	
	@POST("GetPublicAllianceInfo")
	Call<GetPublicAllianceInfoResponse> getPublicAllianceInfo(@Body GetPublicAllianceInfoRequest getPublicAllianceInfoRequest);*/
	
}
