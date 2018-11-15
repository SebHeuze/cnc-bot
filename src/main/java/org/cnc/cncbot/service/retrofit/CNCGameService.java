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
import retrofit2.http.POST;

/**
 * Game CDN Origin services class
 * @author SebHeuze
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

	@POST("RankingGetCount")
	Call<Integer> rankingGetCount(@Body RankingCountRequest rankingGetCountRequest);
	
	@POST("RankingGetData")
	Call<RankingDataResponse> rankingGetData(@Body RankingDataRequest rankingGetDataRequest);
	
	@POST("GetPublicPlayerInfo")
	Call<PublicPlayerInfoResponse> getPublicPlayerInfo(@Body PublicPlayerInfoRequest getPublicPlayerInfoRequest);
	
	@POST("GetPublicAllianceInfo")
	Call<PublicAllianceInfoResponse> getPublicAllianceInfo(@Body PublicAllianceInfoRequest getPublicAllianceInfoRequest);
	
}
