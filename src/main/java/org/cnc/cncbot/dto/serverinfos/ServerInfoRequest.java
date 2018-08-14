package org.cnc.cncbot.dto.serverinfos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServerInfoRequest {
	private String session;
}
