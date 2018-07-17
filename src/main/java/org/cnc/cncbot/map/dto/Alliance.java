package org.cnc.cncbot.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Objet Alliance
 * @author SEB
 *
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Alliance {
    /**
     * Id.
     */
    private int id;
    
    /**
     * Nom.
     */
    private String nom;
    
    /**
     * Points.
     */
    private int points;
    
    /**
     * Nombre joueurs.
     */
    private int nombreJoueurs;
    
    @Override
    public boolean equals(Object obj) {
      Alliance other = (Alliance) obj;
      return this.id == other.id;
    }

    @Override
    public int hashCode() {
       return this.id;
    }
}
