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
public class Coords implements Serializable {
	
	/**
	 * Serial
	 */
	private static final long serialVersionUID = 5538445357672451530L;

	Integer x;

	Integer y;
}
