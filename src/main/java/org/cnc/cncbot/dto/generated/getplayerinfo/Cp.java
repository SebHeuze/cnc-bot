package org.cnc.cncbot.dto.generated.getplayerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cp {

@SerializedName("b")
@Expose
private Integer b;
@SerializedName("d")
@Expose
private Double d;
@SerializedName("m")
@Expose
private Integer m;
@SerializedName("s")
@Expose
private Integer s;

public Integer getB() {
return b;
}

public void setB(Integer b) {
this.b = b;
}

public Double getD() {
return d;
}

public void setD(Double d) {
this.d = d;
}

public Integer getM() {
return m;
}

public void setM(Integer m) {
this.m = m;
}

public Integer getS() {
return s;
}

public void setS(Integer s) {
this.s = s;
}

}