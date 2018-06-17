package org.cnc.cncbot.dto.generated;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class OriginAccountInfo {

@Expose
private Integer Account;
@Expose
private Integer ErrorCode;
@Expose
private Object FirstName;
@Expose
private Object Friends;
@Expose
private Object Language;
@Expose
private Object LastName;
@Expose
private Object Name;
@Expose
private Boolean NewAccount;
@Expose
private Integer RecommendedServer;
@Expose
private List<Server> Servers = new ArrayList<Server>();
@Expose
private String SessionGUID;
@Expose
private Integer Timezone;

/**
* 
* @return
* The Account
*/
public Integer getAccount() {
return Account;
}

/**
* 
* @param Account
* The Account
*/
public void setAccount(Integer Account) {
this.Account = Account;
}

/**
* 
* @return
* The ErrorCode
*/
public Integer getErrorCode() {
return ErrorCode;
}

/**
* 
* @param ErrorCode
* The ErrorCode
*/
public void setErrorCode(Integer ErrorCode) {
this.ErrorCode = ErrorCode;
}

/**
* 
* @return
* The FirstName
*/
public Object getFirstName() {
return FirstName;
}

/**
* 
* @param FirstName
* The FirstName
*/
public void setFirstName(Object FirstName) {
this.FirstName = FirstName;
}

/**
* 
* @return
* The Friends
*/
public Object getFriends() {
return Friends;
}

/**
* 
* @param Friends
* The Friends
*/
public void setFriends(Object Friends) {
this.Friends = Friends;
}

/**
* 
* @return
* The Language
*/
public Object getLanguage() {
return Language;
}

/**
* 
* @param Language
* The Language
*/
public void setLanguage(Object Language) {
this.Language = Language;
}

/**
* 
* @return
* The LastName
*/
public Object getLastName() {
return LastName;
}

/**
* 
* @param LastName
* The LastName
*/
public void setLastName(Object LastName) {
this.LastName = LastName;
}

/**
* 
* @return
* The Name
*/
public Object getName() {
return Name;
}

/**
* 
* @param Name
* The Name
*/
public void setName(Object Name) {
this.Name = Name;
}

/**
* 
* @return
* The NewAccount
*/
public Boolean getNewAccount() {
return NewAccount;
}

/**
* 
* @param NewAccount
* The NewAccount
*/
public void setNewAccount(Boolean NewAccount) {
this.NewAccount = NewAccount;
}

/**
* 
* @return
* The RecommendedServer
*/
public Integer getRecommendedServer() {
return RecommendedServer;
}

/**
* 
* @param RecommendedServer
* The RecommendedServer
*/
public void setRecommendedServer(Integer RecommendedServer) {
this.RecommendedServer = RecommendedServer;
}

/**
* 
* @return
* The Servers
*/
public List<Server> getServers() {
return Servers;
}

/**
* 
* @param Servers
* The Servers
*/
public void setServers(List<Server> Servers) {
this.Servers = Servers;
}

/**
* 
* @return
* The SessionGUID
*/
public String getSessionGUID() {
return SessionGUID;
}

/**
* 
* @param SessionGUID
* The SessionGUID
*/
public void setSessionGUID(String SessionGUID) {
this.SessionGUID = SessionGUID;
}

/**
* 
* @return
* The Timezone
*/
public Integer getTimezone() {
return Timezone;
}

/**
* 
* @param Timezone
* The Timezone
*/
public void setTimezone(Integer Timezone) {
this.Timezone = Timezone;
}

}