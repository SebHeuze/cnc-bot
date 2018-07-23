package org.cnc.cncbot.map.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Player entity
 * @author heuze
 *
 */
@Entity
@Getter
@Setter
@Table(name = "joueur")
public class Player {
	@Id
	@Column(name = "i")
	private Integer id;
	
	@Column(name = "p")
	private Long points = new Long(0);
	
	@Column(name = "a")
	private Integer allianceId = 0;
	
	@Column(name = "n")
	private String name;
	
	@Column(name = "f")
	private Integer faction;
	
	@Column(name = "ps")
	private Integer ps = 0;
	
	@Column(name = "pd")
	private Integer pd = 0;
	
	@Column(name = "bc")
	private Integer baseCount = 0;
    
    @Override
    public boolean equals(Object obj) {
      Player other = (Player) obj;
      return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
       return this.id;
    }
}