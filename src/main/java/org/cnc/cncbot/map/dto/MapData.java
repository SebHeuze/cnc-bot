package org.cnc.cncbot.map.dto;

import java.util.List;
import java.util.Set;

import org.cnc.cncbot.map.entities.Alliance;
import org.cnc.cncbot.map.entities.MapObject;
import org.cnc.cncbot.map.entities.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Object MapData
 * @author SebHeuze
 *
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapData {
    /**
     * Liste Object
     */
    List<MapObject> objectsList;
    
    /**
     * Alliance List
     */
    Set<Alliance> alliancesList;
    
    /**
     * Players List
     */
    Set<Player> playersList;

}
