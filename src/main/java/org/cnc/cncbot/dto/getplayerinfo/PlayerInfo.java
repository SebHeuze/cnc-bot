package org.cnc.cncbot.dto.getplayerinfo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerInfo {

private Integer allianceId;

private Object allianceName;

private List<City> cities = null;

private Integer id;

private String name;

private List<Tech> techs = null;

private Integer worldStartX;

private Integer worldStartY;

private Integer at;

private List<C> c = null;

private Double cd;

private Cp cp;

private List<E> e = null;

private Integer f;

private G g;

private List<Ic> ic = null;

private Integer l;

private Integer lrs;

private Object o;

private Integer p;

private String rafc;

private Integer rt;

private Boolean s;

private Boolean sl;

private Spp spp;

private St st;
}