package org.cnc.cncbot.dto;

/**
 * EndGame types.
 * @author SEB
 *
 */
public enum EndGameType {
  SHIELD(1),
  SATTELITE(2),
  FORTRESS(3);
  
  /**
   * ID type.
   */
  private final int idType;
  
  /**
   * Constructor.
   * @param idType
   */
  private EndGameType(int idType) {
    this.idType = idType;
  }
  
  public int getIdType() {
    return this.idType;
  }
}
