package org.cnc.cncbot.dto.rankingdata;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RankingDataRequest {
	private String session;
	private int view;
	private int firstIndex;
	private int lastIndex;
	private int sortColumn;
	private boolean ascending;
}
