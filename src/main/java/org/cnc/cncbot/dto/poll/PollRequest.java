package org.cnc.cncbot.dto.poll;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PollRequest {
	private String session;
	private String request;
	private int sequenceid;
	private int requestid;
}
