package org.cnc.cncbot.map.dto;

import org.cnc.cncbot.dto.POIType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Objet POI.
 * @author SEB
 *
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class POI extends MapObject {
    /**
     * Type du POI.
     */
    private POIType type;
    
    /**
     * Level du POI.
     */
    private int level;
    
    /**
     * Id de l'alliance.
     */
    private int idAlliance;
    
    
}
