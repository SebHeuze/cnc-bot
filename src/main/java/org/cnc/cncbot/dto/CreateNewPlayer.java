package org.cnc.cncbot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNewPlayer {

private String session;

private Object name;

private Integer faction;

private String startDir;

}