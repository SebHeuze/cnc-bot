package org.cnc.cncbot.dto.generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

@SerializedName("LoginResult")
@Expose
private LoginResult loginResult;

public LoginResult getLoginResult() {
return loginResult;
}

public void setLoginResult(LoginResult loginResult) {
this.loginResult = loginResult;
}

}