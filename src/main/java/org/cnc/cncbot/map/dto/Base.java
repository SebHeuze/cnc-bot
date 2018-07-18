package org.cnc.cncbot.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Objet Base.
 * @author SEB
 *
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Base extends MapObject {
    /**
     * Id.
     */
    private int id;
    
    /**
     * Id player.
     */
    private int idJoueur;
    
    /**
     * Name.
     */
    private String name;
    
    /**
     * level.
     */
    private int level;
    
    /**
     * Début support alert.
     */
    private int supportAlertStart;

    /**
     * CoolDown de déplacement.
     */
    private int moveCoolDownEndStep;
    
    /**
     * Bloquage autour d'une coordoonée pour combien de temps.
     */
    private int moveRestrictionEndStep;
    
    /**
     * Bloqué sur quelle coordonnée?    
     */
    private int moveRestrictionCoord;
    
    /**
     * Recovery fin.
     */
    private int recoveryEndStep;
    
    /**
     * Fin support alert.
     */
    private int supportAlertEnd;
    
    /**
     * durée protection en millisecondes.
     */
    private int protectionEnd;
    
    /**
     * durée protection en millisecondes.
     */
    private int lockDown;
    
    /**
     * condition des batiments.
     */
    private int conditionBatiments;
    
    /**
     * conditionDefense.
     */
    private int conditionDefense = -1;

    /**
     * As t'il une protection.
     * @return
     */
    public boolean hasProtectionEnd() {
      return this.protectionEnd != 0;
    }
    
    /**
     * As t'il une alerte support.
     * @return
     */
    public boolean hasSupportAlert() {
      return this.supportAlertEnd != 0 || this.supportAlertStart != 0;
    }

}
