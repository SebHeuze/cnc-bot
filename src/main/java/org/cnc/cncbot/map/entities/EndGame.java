package org.cnc.cncbot.map.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

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
@Table(name="endgame")
public class EndGame extends MapObject {
	private Integer type;
	private Integer  step = 0;
	
}