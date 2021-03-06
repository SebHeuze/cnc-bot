package org.cnc.cncbot.dto.publicallianceinfo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PublicAllianceInfoResponse {

private String a;

private Integer bd;

private Integer bde;

private Integer bdp;

private String d;

private Long egwr;

private Long egws;

private String f;

private Integer fac;

private Integer fd;

private Integer i;

private List<M> m = new ArrayList<M>();

private Integer mc;

private Long ms;

private Long ms4;

private String n;

private Integer nb;

private List<Opoi> opois = new ArrayList<Opoi>();

private Integer poi;

private Integer r;

private List<Rpoi> rpois = new ArrayList<Rpoi>();

}