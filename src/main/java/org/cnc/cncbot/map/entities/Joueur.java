package org.cnc.cncbot.map.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * Player entity
 * @author heuze
 *
 */
@Entity
@Getter
@Setter
public class Joueur {
	private Integer i;
	private Long p;
	private Integer a;
	private String n;
	private Integer f;
	private Integer ps;
	private Integer pd;
	private Integer bc;
}