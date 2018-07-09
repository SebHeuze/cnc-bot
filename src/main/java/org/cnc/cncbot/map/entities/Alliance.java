package org.cnc.cncbot.map.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * Alliance entity
 * @author heuze
 *
 */
@Entity
@Getter
@Setter
public class Alliance {
	@Id
	private Long a;
	private String an;
	private String p;
	private Long c;
}