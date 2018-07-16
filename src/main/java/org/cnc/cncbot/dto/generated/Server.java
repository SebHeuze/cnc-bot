package org.cnc.cncbot.dto.generated;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

@Generated("org.jsonschema2pojo")
@Getter
@Setter
public class Server {

@Expose
private Boolean AcceptNewPlayer;
@Expose
private Integer Account;
@Expose
private Integer Changelist;
@Expose
private String Description;
@Expose
private String ExtendedLanguage;
@Expose
private Integer Faction;
@Expose
private List<Object> Friends = new ArrayList<Object>();
@Expose
private Integer Id;
@Expose
private List<Object> Invites = new ArrayList<Object>();
@Expose
private Boolean IsVeteranServer;
@Expose
private Long LastSeen;
@Expose
private Integer MaxPlayers;
@Expose
private String Name;
@Expose
private Boolean Online;
@Expose
private Integer PlayerCount;
@Expose
private Boolean Recommended;
@Expose
private Long StartTime;
@Expose
private Integer Timezone;
@Expose
private String Url;

}