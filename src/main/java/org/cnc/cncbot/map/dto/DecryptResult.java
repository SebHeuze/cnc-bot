package org.cnc.cncbot.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Résulat crypt.
 * @author SebHeuze
 *
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
public class DecryptResult {
    
    /**
     * Index courant de décryptage.
     */
    private int currentIndex;
    
    /**
     * Résultat.
     */
    private int result;
}
