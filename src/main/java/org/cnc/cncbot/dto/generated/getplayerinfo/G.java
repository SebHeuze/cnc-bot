package org.cnc.cncbot.dto.generated.getplayerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class G {

@SerializedName("b")
@Expose
private Double b;
@SerializedName("d")
@Expose
private Double d;
@SerializedName("s")
@Expose
private Integer s;

public Double getB() {
return b;
}

public void setB(Double b) {
this.b = b;
}

public Double getD() {
return d;
}

public void setD(Double d) {
this.d = d;
}

public Integer getS() {
return s;
}

public void setS(Integer s) {
this.s = s;
}

}