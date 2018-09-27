package org.cnc.cncbot.dto.rankingdata;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingDataResponse {

private List<P> p = new ArrayList<P>();

private List<A> a = new ArrayList<A>();

private Integer r;

}