package org.cnc.cncbot.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Objet MapObject.
 * @author SEB
 *
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapObject {
    /**
     * Coord x.
     */
    private int x;
    
    /**
     * Coord y.
     */
    private int y;
    
    /**
     * Hash
     */
    @Override
    public int hashCode() {
      return this.x | this.y << 16;
    }
    
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
           return other.x == this.x && other.y == this.y;
    }


}
