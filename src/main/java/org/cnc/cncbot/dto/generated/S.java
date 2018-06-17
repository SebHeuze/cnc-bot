package org.cnc.cncbot.dto.generated;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class S {

@Expose
private List<String> a = new ArrayList<String>();
@Expose
private List<String> d = new ArrayList<String>();
@Expose
private Integer i;
@Expose
private List<String> p = new ArrayList<String>();
@Expose
private Integer v;

/**
* 
* @return
* The a
*/
public List<String> getA() {
return a;
}

/**
* 
* @param a
* The a
*/
public void setA(List<String> a) {
this.a = a;
}

/**
* 
* @return
* The d
*/
public List<String> getD() {
return d;
}

/**
* 
* @param d
* The d
*/
public void setD(List<String> d) {
this.d = d;
}

/**
* 
* @return
* The i
*/
public Integer getI() {
return i;
}

/**
* 
* @param i
* The i
*/
public void setI(Integer i) {
this.i = i;
}

/**
* 
* @return
* The p
*/
public List<String> getP() {
return p;
}

/**
* 
* @param p
* The p
*/
public void setP(List<String> p) {
this.p = p;
}

/**
* 
* @return
* The v
*/
public Integer getV() {
return v;
}

/**
* 
* @param v
* The v
*/
public void setV(Integer v) {
this.v = v;
}

}