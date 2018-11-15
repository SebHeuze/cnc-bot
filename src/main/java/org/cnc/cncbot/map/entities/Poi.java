package org.cnc.cncbot.map.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Poi entity
 * @author SebHeuze
 *
 */
@Entity
@Getter
@Setter
@Table(name="poi")
public class Poi extends MapObject {
	@Column(name = "t")
	private Integer  type;

	@Column(name = "l")
	private Integer level;
	
	@Column(name = "a")
	private Integer allianceId = 0;

}