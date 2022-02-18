package org.cnc.cncbot.stats.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the base database table.
 * 
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="stats_base")
public class StatsBase implements Serializable, Persistable<Integer>  {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	
	@Column(name="coord_x")
	private Integer coordX;

	@Column(name="coord_y")
	private Integer coordY;

	@Column(name="id_joueur")
	private Integer playerId;

	@Column(name="nom_base")
	private String name;

	@Column(name="score_base")
	private Long score;

	/**
	 * We are doing to stop hibernate from doing select to check if exist before insert
	 * Perfomance improvement since we delete all result before insert
	 */
	@Override
	public boolean isNew() {
		return true;
	}
	
	@Override
    public boolean equals(Object obj) {
		if (obj instanceof StatsBase) {
			StatsBase other = (StatsBase) obj;
		      return this.id.equals(other.id);
		} 
		return false;
    }

    @Override
    public int hashCode() {
       return this.id.hashCode();
    }
	
}