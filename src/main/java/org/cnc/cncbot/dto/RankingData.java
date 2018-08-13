package org.cnc.cncbot.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingData {

private List<P> p = new ArrayList<P>();

private List<A> a = new ArrayList<A>();

private Integer r;

}