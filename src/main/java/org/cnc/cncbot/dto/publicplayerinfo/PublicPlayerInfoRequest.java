package org.cnc.cncbot.dto.publicplayerinfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PublicPlayerInfoRequest {
	private String session;
	private int id;
}
