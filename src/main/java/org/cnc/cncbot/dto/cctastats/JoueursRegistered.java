package org.cnc.cncbot.dto.cctastats;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class JoueursRegistered {

	@SerializedName("id_joueur")
	private Integer idJoueur;

	@SerializedName("0")	
	private Integer _0;

}