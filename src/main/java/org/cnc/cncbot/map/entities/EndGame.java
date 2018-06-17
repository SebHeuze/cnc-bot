package org.cnc.cncbot.map.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * Endgame entity
 * @author heuze
 *
 */
@Entity
@Getter
@Setter
public class EndGame {
	private Integer type;
	private Integer x;
	private Integer y;
	private Integer  step;
	
}