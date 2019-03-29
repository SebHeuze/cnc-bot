package org.cnc.cncbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
public class UserSession {

	private String user;
	
	private String password;
	
	private int worldId;
	
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
