package org.cnc.cncbot.map.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

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
public class Settings {
	@Id
	private String name;
	private String value;
}