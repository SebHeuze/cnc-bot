package org.cnc.cncbot.dto.generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResult {

@SerializedName("ReturnCode")
@Expose
private Integer returnCode;
@SerializedName("SessionGuid")
@Expose
private String sessionGuid;

public Integer getReturnCode() {
return returnCode;
}

public void setReturnCode(Integer returnCode) {
this.returnCode = returnCode;
}

public String getSessionGuid() {
return sessionGuid;
}

public void setSessionGuid(String sessionGuid) {
this.sessionGuid = sessionGuid;
}

}