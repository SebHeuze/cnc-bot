package org.cnc.cncbot.dto.generated;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class JoueursRegistered {

@SerializedName("id_joueur")
@Expose
private Integer idJoueur;
@SerializedName("0")
@Expose
private Integer _0;

/**
* 
* @return
* The idJoueur
*/
public Integer getIdJoueur() {
return idJoueur;
}

/**
* 
* @param idJoueur
* The id_joueur
*/
public void setIdJoueur(Integer idJoueur) {
this.idJoueur = idJoueur;
}

/**
* 
* @return
* The _0
*/
public Integer get0() {
  return _0;
}

/**
* 
* @param _0
* The 0
*/
public void set0(Integer _0) {
  this._0 = _0;
}

}