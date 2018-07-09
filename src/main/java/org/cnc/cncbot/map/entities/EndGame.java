package org.cnc.cncbot.map.entities;

import java.io.Serializable;

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
public class EndGame implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer x;
	@Id
	private Integer y;
	private Integer type;
	private Integer  step;
	
}