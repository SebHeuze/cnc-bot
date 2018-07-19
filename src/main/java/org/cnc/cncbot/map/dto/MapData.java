package org.cnc.cncbot.map.dto;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Object MapData
 * @author SEB
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
