package org.cnc.cncbot.dto.opensession;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OpenSessionRequest {
	@Builder.Default private int platformId = 1;
	@Builder.Default private int refId = -1;
	@Builder.Default private boolean reset = true;
	private String session;
	@Builder.Default private int version = -1;
}
