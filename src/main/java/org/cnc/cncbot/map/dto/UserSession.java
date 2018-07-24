package org.cnc.cncbot.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSession {

	private int requestId = 0;
	
	private int sequenceId = 0;
	
	private String gameSessionId;
	
	private String playerName;
	
	private String sessionId;
	
	public int useRequestId() {
		return requestId++;
	}
	
	public int useSequenceId() {
		return sequenceId++;
	}
}
