package org.cnc.cncbot.map.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

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
public class Base  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer i;
	private Integer pi;
	private Integer y;
	private Integer x;
	private String  n;
	private Integer l;
	private Boolean al;
	private Boolean pr;
	private Integer cb;
	private Integer cd;
	private Long ps;
	
}