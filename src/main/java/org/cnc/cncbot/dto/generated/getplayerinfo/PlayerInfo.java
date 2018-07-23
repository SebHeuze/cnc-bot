package org.cnc.cncbot.dto.generated.getplayerinfo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerInfo {

@SerializedName("AllianceId")
@Expose
private Integer allianceId;
@SerializedName("AllianceName")
@Expose
private Object allianceName;
@SerializedName("Cities")
@Expose
private List<City> cities = null;
@SerializedName("Id")
@Expose
private Integer id;
@SerializedName("Name")
@Expose
private String name;
@SerializedName("Techs")
@Expose
private List<Tech> techs = null;
@SerializedName("WorldStartX")
@Expose
private Integer worldStartX;
@SerializedName("WorldStartY")
@Expose
private Integer worldStartY;
@SerializedName("at")
@Expose
private Integer at;
@SerializedName("c")
@Expose
private List<C> c = null;
@SerializedName("cd")
@Expose
private Double cd;
@SerializedName("cp")
@Expose
private Cp cp;
@SerializedName("e")
@Expose
private List<E> e = null;
@SerializedName("f")
@Expose
private Integer f;
@SerializedName("g")
@Expose
private G g;
@SerializedName("ic")
@Expose
private List<Ic> ic = null;
@SerializedName("l")
@Expose
private Integer l;
@SerializedName("lrs")
@Expose
private Integer lrs;
@SerializedName("o")
@Expose
private Object o;
@SerializedName("p")
@Expose
private Integer p;
@SerializedName("rafc")
@Expose
private String rafc;
@SerializedName("rt")
@Expose
private Integer rt;
@SerializedName("s")
@Expose
private Boolean s;
@SerializedName("sl")
@Expose
private Boolean sl;
@SerializedName("spp")
@Expose
private Spp spp;
@SerializedName("st")
@Expose
private St st;

public Integer getAllianceId() {
return allianceId;
}

public void setAllianceId(Integer allianceId) {
this.allianceId = allianceId;
}

public Object getAllianceName() {
return allianceName;
}

public void setAllianceName(Object allianceName) {
this.allianceName = allianceName;
}

public List<City> getCities() {
return cities;
}

public void setCities(List<City> cities) {
this.cities = cities;
}

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public List<Tech> getTechs() {
return techs;
}

public void setTechs(List<Tech> techs) {
this.techs = techs;
}

public Integer getWorldStartX() {
return worldStartX;
}

public void setWorldStartX(Integer worldStartX) {
this.worldStartX = worldStartX;
}

public Integer getWorldStartY() {
return worldStartY;
}

public void setWorldStartY(Integer worldStartY) {
this.worldStartY = worldStartY;
}

public Integer getAt() {
return at;
}

public void setAt(Integer at) {
this.at = at;
}

public List<C> getC() {
return c;
}

public void setC(List<C> c) {
this.c = c;
}

public Double getCd() {
return cd;
}

public void setCd(Double cd) {
this.cd = cd;
}

public Cp getCp() {
return cp;
}

public void setCp(Cp cp) {
this.cp = cp;
}

public List<E> getE() {
return e;
}

public void setE(List<E> e) {
this.e = e;
}

public Integer getF() {
return f;
}

public void setF(Integer f) {
this.f = f;
}

public G getG() {
return g;
}

public void setG(G g) {
this.g = g;
}

public List<Ic> getIc() {
return ic;
}

public void setIc(List<Ic> ic) {
this.ic = ic;
}

public Integer getL() {
return l;
}

public void setL(Integer l) {
this.l = l;
}

public Integer getLrs() {
return lrs;
}

public void setLrs(Integer lrs) {
this.lrs = lrs;
}

public Object getO() {
return o;
}

public void setO(Object o) {
this.o = o;
}

public Integer getP() {
return p;
}

public void setP(Integer p) {
this.p = p;
}

public String getRafc() {
return rafc;
}

public void setRafc(String rafc) {
this.rafc = rafc;
}

public Integer getRt() {
return rt;
}

public void setRt(Integer rt) {
this.rt = rt;
}

public Boolean getS() {
return s;
}

public void setS(Boolean s) {
this.s = s;
}

public Boolean getSl() {
return sl;
}

public void setSl(Boolean sl) {
this.sl = sl;
}

public Spp getSpp() {
return spp;
}

public void setSpp(Spp spp) {
this.spp = spp;
}

public St getSt() {
return st;
}

public void setSt(St st) {
this.st = st;
}

}