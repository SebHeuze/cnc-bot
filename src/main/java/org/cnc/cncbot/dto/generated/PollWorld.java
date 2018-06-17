package org.cnc.cncbot.dto.generated;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.ToString;

@ToString
@Generated("org.jsonschema2pojo")
public class PollWorld {

@SerializedName("__type")
@Expose
private String Type;
@Expose
private List<S> s = new ArrayList<S>();
@Expose
private List<String> ch = new ArrayList<String>();

/**
* 
* @return
* The a
*/
public List<String> getCH() {
return ch;
}

/**
* 
* @param a
* The a
*/
public void setCH(List<String> ch) {
this.ch = ch;
}

/**
* 
* @return
* The Type
*/
public String getType() {
return Type;
}

/**
* 
* @param Type
* The __type
*/
public void setType(String Type) {
this.Type = Type;
}

/**
* 
* @return
* The s
*/
public List<S> getS() {
return s;
}

/**
* 
* @param s
* The s
*/
public void setS(List<S> s) {
this.s = s;
}

}