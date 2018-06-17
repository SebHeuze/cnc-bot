package org.cnc.cncbot.dto.generated.getplayerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {

@SerializedName("h")
@Expose
private Integer h;
@SerializedName("i")
@Expose
private Integer i;
@SerializedName("n")
@Expose
private String n;
@SerializedName("r")
@Expose
private String r;
@SerializedName("w")
@Expose
private Integer w;
@SerializedName("x")
@Expose
private Integer x;
@SerializedName("y")
@Expose
private Integer y;

public Integer getH() {
return h;
}

public void setH(Integer h) {
this.h = h;
}

public Integer getI() {
return i;
}

public void setI(Integer i) {
this.i = i;
}

public String getN() {
return n;
}

public void setN(String n) {
this.n = n;
}

public String getR() {
return r;
}

public void setR(String r) {
this.r = r;
}

public Integer getW() {
return w;
}

public void setW(Integer w) {
this.w = w;
}

public Integer getX() {
return x;
}

public void setX(Integer x) {
this.x = x;
}

public Integer getY() {
return y;
}

public void setY(Integer y) {
this.y = y;
}

}