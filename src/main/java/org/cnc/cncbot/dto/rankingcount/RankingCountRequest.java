package org.cnc.cncbot.dto.rankingcount;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RankingCountRequest {
	private String session;
	private int view;
	private int rankingType;
}
