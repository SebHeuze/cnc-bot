package org.cnc.cncbot.dto.generated.getplayerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tech {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("l")
@Expose
private Integer l;
@SerializedName("mid")
@Expose
private Integer mid;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public Integer getL() {
return l;
}

public void setL(Integer l) {
this.l = l;
}

public Integer getMid() {
return mid;
}

public void setMid(Integer mid) {
this.mid = mid;
}

}