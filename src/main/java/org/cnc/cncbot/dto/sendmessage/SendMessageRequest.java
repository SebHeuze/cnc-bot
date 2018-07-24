package org.cnc.cncbot.dto.sendmessage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendMessageRequest {
	private String session;
	@Builder.Default
	private String alliances = "";
	private String body;
	private String players;
	private String subject;
}
