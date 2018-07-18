package org.cnc.cncbot.map.dto;

import org.cnc.cncbot.dto.EndGameType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Objet EndGame.
 * @author SEB
 *
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EndGame extends MapObject {
    /**
     * Type Endgame.
     */
    private EndGameType type;
    
    /**
     * Step Endgame.
     */
    private int step;
    
    
}
