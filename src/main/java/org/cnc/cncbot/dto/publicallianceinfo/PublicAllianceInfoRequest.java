package org.cnc.cncbot.dto.publicallianceinfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PublicAllianceInfoRequest {
	private String session;
	private int id;
}
