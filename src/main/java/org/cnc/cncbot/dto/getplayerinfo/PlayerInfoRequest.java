package org.cnc.cncbot.dto.getplayerinfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlayerInfoRequest {
	private String session;
}
