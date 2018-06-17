package org.cnc.cncbot.dto.generated;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class RankingData {

@Expose
private List<P> p = new ArrayList<P>();
@Expose
private List<A> a = new ArrayList<A>();
@Expose
private Integer r;

/**
* 
* @return
* The p
*/
public List<P> getP() {
return p;
}

/**
* 
* @param p
* The p
*/
public void setP(List<P> p) {
this.p = p;
}

/**
* 
* @return
* The a
*/
public List<A> getA() {
return a;
}

/**
* 
* @param a
* The a
*/
public void setA(List<A> a) {
this.a = a;
}

/**
* 
* @return
* The r
*/
public Integer getR() {
return r;
}

/**
* 
* @param r
* The r
*/
public void setR(Integer r) {
this.r = r;
}

}