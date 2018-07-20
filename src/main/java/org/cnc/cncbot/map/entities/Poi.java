package org.cnc.cncbot.map.entities;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

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
	
	@EmbeddedId
	PoiId id;
	
	private String  t;
	private Integer l;
	private Integer a;
}