package org.cnc.cncbot.map.entities;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Poi entity
 * @author heuze
 *
 */
@Entity
@Getter
@Setter
public class Poi {
	private String name;
	private String value;
}