package org.cnc.cncbot.map.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
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
	private Boolean alert;

	@Column(name = "pr")
	private Boolean hasProtectionEnd;

	@Column(name = "cb")
	private Integer conditionBuilding;

	@Column(name = "cd")
	private Integer conditionDefense;

	@Column(name = "ps")
	private Integer protectionEnd;
	
}