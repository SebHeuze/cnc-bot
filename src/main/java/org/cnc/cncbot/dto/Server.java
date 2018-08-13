package org.cnc.cncbot.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Server {

private Boolean AcceptNewPlayer;

private Integer Account;

private Integer Changelist;

private String Description;

private String ExtendedLanguage;

private Integer Faction;

private List<Object> Friends = new ArrayList<Object>();

private Integer Id;

private List<Object> Invites = new ArrayList<Object>();

private Boolean IsVeteranServer;

private Long LastSeen;

private Integer MaxPlayers;

private String Name;

private Boolean Online;

private Integer PlayerCount;

private Boolean Recommended;

private Long StartTime;

private Integer Timezone;

private String Url;

}