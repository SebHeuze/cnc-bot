
package org.cnc.cncbot.dto.generated.getplayerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class St {

@SerializedName("ll")
@Expose
private Integer ll;
@SerializedName("sd")
@Expose
private Integer sd;

public Integer getLl() {
return ll;
}

public void setLl(Integer ll) {
this.ll = ll;
}

public Integer getSd() {
return sd;
}

public void setSd(Integer sd) {
this.sd = sd;
}

}