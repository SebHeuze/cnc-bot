package org.cnc.cncbot.map.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Player base entity
 * @author heuze
 *
 */
@Entity
@Getter
@Setter
@Table(name="base")
public class Base  extends MapObject {
	@Column(name = "i")
	private Integer id;

	@Column(name = "pi")
	private Integer playerId;

	@Column(name = "n")
	private String  name;

	@Column(name = "l")
	private Integer level;

	@Column(name = "al")
	private Boolean alert = false;

	@Column(name = "pr")
	private Boolean hasProtectionEnd = false;

	@Column(name = "cb")
	private Integer conditionBuilding = 0;;

	@Column(name = "cd")
	private Integer conditionDefense = 0;;

	@Column(name = "ps")
	private Integer protectionEnd = 0;
	
}