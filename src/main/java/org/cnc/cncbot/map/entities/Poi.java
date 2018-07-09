package org.cnc.cncbot.map.entities;

import java.io.Serializable;

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
public class Poi implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer y;
	@Id
	private Integer x;
	private String  t;
	private Integer l;
	private Integer a;
}