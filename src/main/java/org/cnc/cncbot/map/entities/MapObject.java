package org.cnc.cncbot.map.entities;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.springframework.data.domain.Persistable;

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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class MapObject implements Persistable<Coords>{
	
	@EmbeddedId
	Coords coords;
	

    /**
     * Equals
     */
    @Override
    public boolean equals(Object obj) {
           if (this == obj) return true;
           if (obj == null || this.getClass() != obj.getClass()){
                  return false;
           }
           MapObject other = (MapObject) obj;
           return other.coords.x.equals(this.coords.x) && other.coords.y.equals(this.coords.y);
    }
    
    /**
     * Hash
     */
    @Override
    public int hashCode() {
      return this.coords.x | this.coords.y<< 16;
    }
    
	/**
	 * We are doing to stop hibernate from doing select to check if exist before insert
	 * Perfomance improvement since we delete all result before insert
	 */
	@Override
	public boolean isNew() {
		return true;
	}

	@Override
	public Coords getId() {
		return this.coords;
	}
}