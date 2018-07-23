package org.cnc.cncbot.map.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Alliance entity
 * @author heuze
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="alliance")
public class Alliance {
	@Id
	@Column(name = "a")
	private Integer allianceId;
	
	@Column(name = "an")
	private String name;
	
	@Column(name = "p")
	private Long points = new Long(0);
	
	@Column(name = "c")
	private Integer nbPlayers;
	
    @Override
    public boolean equals(Object obj) {
      Alliance other = (Alliance) obj;
      return this.allianceId.equals(other.allianceId);
    }

    @Override
    public int hashCode() {
       return this.allianceId.hashCode();
    }
}