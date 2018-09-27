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
 * The persistent class for the poi database table.
 * 
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="stats_poi")
public class StatsPoi implements Serializable, Persistable<Integer> {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="coord_x")
	private Integer coordX;

	@Column(name="coord_y")
	private Integer coordY;

	@Column(name="id_alliance")
	private Integer idAlliance;

	private Integer level;

	private Integer type;
	
	/**
	 * We are doing to stop hibernate from doing select to check if exist before insert
	 * Perfomance improvement since we delete all result before insert
	 */
	@Override
	public boolean isNew() {
		return true;
	}
	
}