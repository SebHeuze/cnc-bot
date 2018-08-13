package org.cnc.cncbot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResult {

private Integer returnCode;

private String sessionGuid;
}