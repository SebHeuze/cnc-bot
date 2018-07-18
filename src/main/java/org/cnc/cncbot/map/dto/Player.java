package org.cnc.cncbot.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Objet Joueur.
 * @author SEB
 *
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    /**
     * Id.
     */
    private int id;
    
    /**
     * Score.
     */
    private int score;
    
    /**
     * Alliance.
     */
    private int idAlliance;
    
    /**
     * Pseudo.
     */
    private String username;
    
    /**
     * Faction.
     */
    private int faction;
    
    /**
     * ps.
     */
    private int ps;
    
    /**
     * pd.
     */
    private int pd;
    
    /**
     * Nombre bases.
     */
    private int nbBases;
    
    @Override
    public boolean equals(Object obj) {
      Player other = (Player) obj;
      return this.id == other.id;
    }

    @Override
    public int hashCode() {
       return this.id;
    }
}
