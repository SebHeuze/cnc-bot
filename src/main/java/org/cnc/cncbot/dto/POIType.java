package org.cnc.cncbot.dto;

/**
 * POI Types.
 * @author SebHeuze
 *
 */
public enum POIType {
  SORTIE_TUNNEL(0),
  TIBERIUM(1),
  CRYSTAL(2),
  POWER(3),
  INFANTRY(4),
  VEHICLE(5),
  AIRCRAFT(6),
  DEFENSE(7);
  
  /**
   * ID type.
   */
  private final int idType;
  
  /**
   * Constructor.
   * @param idType
   */
  private POIType(int idType) {
    this.idType = idType;
  }
  
  public int getIdType() {
    return this.idType;
  }
}
