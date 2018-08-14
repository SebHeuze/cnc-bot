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
	
	 /**
     * Equals
     */
    @Override
    public boolean equals(Object obj) {
           if (this == obj) return true;
           if (obj == null || this.getClass() != obj.getClass()){
                  return false;
           }
           Coords other = (Coords) obj;
           return other.x.equals(this.x) && other.y.equals(this.y);
    }
    
    /**
     * Hash
     */
    @Override
    public int hashCode() {
      return this.x | this.y<< 16;
    }
    
}
