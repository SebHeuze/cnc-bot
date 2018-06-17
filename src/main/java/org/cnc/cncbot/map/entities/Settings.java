package org.cnc.cncbot.map.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * Settings entity
 * @author heuze
 *
 */
@Entity
@Getter
@Setter
public class Settings {
	private Integer y;
	private Integer x;
	private String  t;
	private Integer l;
	private Integer a;
}