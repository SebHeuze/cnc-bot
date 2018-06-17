package org.cnc.cncbot.dto.generated.getplayerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class E {

@SerializedName("i")
@Expose
private Integer i;
@SerializedName("mdbi")
@Expose
private Integer mdbi;
@SerializedName("s")
@Expose
private Integer s;

public Integer getI() {
return i;
}

public void setI(Integer i) {
this.i = i;
}

public Integer getMdbi() {
return mdbi;
}

public void setMdbi(Integer mdbi) {
this.mdbi = mdbi;
}

public Integer getS() {
return s;
}

public void setS(Integer s) {
this.s = s;
}

}