package org.cnc.cncbot.dto.generated.getplayerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class C {

@SerializedName("n")
@Expose
private String n;
@SerializedName("v")
@Expose
private Boolean v;

public String getN() {
return n;
}

public void setN(String n) {
this.n = n;
}

public Boolean getV() {
return v;
}

public void setV(Boolean v) {
this.v = v;
}

}