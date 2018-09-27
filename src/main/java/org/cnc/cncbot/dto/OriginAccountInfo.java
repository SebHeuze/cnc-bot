package org.cnc.cncbot.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OriginAccountInfo {

private Integer Account;

private Integer ErrorCode;

private Object FirstName;

private Object Friends;

private Object Language;

private Object LastName;

private Object Name;

private Boolean NewAccount;

private Integer RecommendedServer;

private List<Server> Servers = new ArrayList<Server>();

private String SessionGUID;

private Integer Timezone;

}