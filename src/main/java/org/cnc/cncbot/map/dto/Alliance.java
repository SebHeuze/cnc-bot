package org.cnc.cncbot.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Alliance Object
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
     * Name.
     */
    private String name;
    
    /**
     * Points.
     */
    private int score;
    
    /**
     * Number of players.
     */
    private int nbPlayer;
    
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
