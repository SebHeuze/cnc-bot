package org.cnc.cncbot.map.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PoiId implements Serializable {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 4280714718242640231L;
	
	private Integer y;
	
	private Integer x;
}
