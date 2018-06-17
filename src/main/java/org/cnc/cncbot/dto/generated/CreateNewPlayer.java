package org.cnc.cncbot.dto.generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateNewPlayer {

@SerializedName("session")
@Expose
private String session;
@SerializedName("name")
@Expose
private Object name;
@SerializedName("faction")
@Expose
private Integer faction;
@SerializedName("startDir")
@Expose
private String startDir;

public String getSession() {
return session;
}

public void setSession(String session) {
this.session = session;
}

public Object getName() {
return name;
}

public void setName(Object name) {
this.name = name;
}

public Integer getFaction() {
return faction;
}

public void setFaction(Integer faction) {
this.faction = faction;
}

public String getStartDir() {
return startDir;
}

public void setStartDir(String startDir) {
this.startDir = startDir;
}

}