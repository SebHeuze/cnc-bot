
package org.cnc.cncbot.dto.generated.getplayerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ic {

@SerializedName("i")
@Expose
private Integer i;
@SerializedName("s")
@Expose
private Integer s;

public Integer getI() {
return i;
}

public void setI(Integer i) {
this.i = i;
}

public Integer getS() {
return s;
}

public void setS(Integer s) {
this.s = s;
}

}