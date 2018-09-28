package org.cnc.cncbot.stats.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.cnc.cncbot.dto.UserSession;
import org.cnc.cncbot.dto.publicallianceinfo.Opoi;
import org.cnc.cncbot.dto.publicallianceinfo.PublicAllianceInfoResponse;
import org.cnc.cncbot.dto.publicallianceinfo.Rpoi;
import org.cnc.cncbot.dto.publicplayerinfo.C;
import org.cnc.cncbot.dto.publicplayerinfo.Ew;
import org.cnc.cncbot.dto.publicplayerinfo.PublicPlayerInfoResponse;
import org.cnc.cncbot.dto.rankingdata.A;
import org.cnc.cncbot.dto.rankingdata.P;
import org.cnc.cncbot.service.GameService;
import org.cnc.cncbot.service.MapService;
import org.cnc.cncbot.stats.entities.StatsAlliance;
import org.cnc.cncbot.stats.entities.StatsBase;
import org.cnc.cncbot.stats.entities.StatsPlayer;
import org.cnc.cncbot.stats.entities.StatsPoi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Component
@Setter
public class StatsAsyncTasks {
	
	private  GameService gameService; 
	
	/**
	 * Get Player public detailled infos
	 * @param userSession
	 * @param rankingDataP
	 * @return
	 * @throws InterruptedException
	 */
	@Async
	public CompletableFuture<StatsPlayer> getPlayerPublicInfos(UserSession userSession, P rankingDataP) throws InterruptedException {

		PublicPlayerInfoResponse playerInfo = this.gameService.getPublicPlayerInfoRequest(userSession, rankingDataP.getP());
		List<StatsBase> basesList = new ArrayList<StatsBase>();

		//get bases of player
		for (C base : playerInfo.getC()){
			StatsBase baseTmp = new StatsBase(base.getI(), base.getX(), base.getY(), rankingDataP.getP(), base.getN(), base.getP());
			basesList.add(baseTmp);
		}

		List<Ew> ewFirstRank = new ArrayList<Ew>();
		if (playerInfo.getEw() != null){
			for (Ew serverWon : playerInfo.getEw()) {
				if (serverWon.getR() == 1) {
					ewFirstRank.add(serverWon);
				}
			}
		}

		StatsPlayer playerTmp = new StatsPlayer(rankingDataP.getP(), playerInfo.getBde(), playerInfo.getBd(), Integer.valueOf((int) Math.round(playerInfo.getDccc())),
				rankingDataP.getF(), rankingDataP.getA(), ewFirstRank.size(), rankingDataP.getPn(), rankingDataP.getR(),
				rankingDataP.getS() == null ? 0 : rankingDataP.getS(), playerInfo.getBde() + playerInfo.getBd(), basesList);


		return CompletableFuture.completedFuture(playerTmp);
	}

	/**
	 * Get public alliance infos
	 * @param rankingDataA
	 * @return
	 */
	@Async
	public CompletableFuture<StatsAlliance> getAlliancePublicInfos(UserSession userSession, A rankingDataA) {
		List<StatsAlliance> result = new ArrayList<StatsAlliance>(); // this is some custom bean holding your result

		PublicAllianceInfoResponse allianceInfo = this.gameService.getPublicAllianceInfoRequest(userSession, rankingDataA.getA());

		//We get rank and POI Score
		List<Rpoi> listeInfosPois = allianceInfo.getRpois();
		int[] rangsPoi = new int[8];
		long[] scoresPoi = new long[8];
		int indexTmp = 1;
		for (Rpoi poiInfo : listeInfosPois) {
			rangsPoi[indexTmp] = poiInfo.getR();
			scoresPoi[indexTmp] = poiInfo.getS();
			indexTmp++;
		}

		List<StatsPoi> listePois = new ArrayList<StatsPoi>();

		//Get alliance Poi
		for (Opoi poi : allianceInfo.getOpois()) {
			StatsPoi poiTmp = new StatsPoi(poi.getI(), poi.getX(), poi.getY(), rankingDataA.getA(), poi.getL(), poi.getT()-1);
			listePois.add(poiTmp);
		}
		StatsAlliance allianceTmp = 
				new StatsAlliance(rankingDataA.getA(), allianceInfo.getD(), rankingDataA.getSa() == null ? 0 : rankingDataA.getSa(),
						allianceInfo.getBdp(), allianceInfo.getBde(), allianceInfo.getBd(), 0, 
						allianceInfo.getPoi(), rankingDataA.getAn(), rankingDataA.getBc() == null ? 0 : rankingDataA.getBc(), rankingDataA.getPc(), rankingDataA.getR(),
						rangsPoi[1], rangsPoi[2], rangsPoi[3], rangsPoi[4], rangsPoi[5],
						rangsPoi[6], rangsPoi[7], rankingDataA.getSc() == null ? 0 : rankingDataA.getSc(), scoresPoi[1], scoresPoi[2],
						scoresPoi[3], scoresPoi[4], scoresPoi[5], scoresPoi[6], scoresPoi[7],
						rankingDataA.getS() == null ? 0 : rankingDataA.getS(), listePois);

		result.add(allianceTmp);

		return CompletableFuture.completedFuture(allianceTmp);
	}
}
