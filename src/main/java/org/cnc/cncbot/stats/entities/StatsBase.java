package org.cnc.cncbot.stats.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
public class StatsBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator (name = "stats_base_id", sequenceName = "stats_base_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stats_base_id")
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
	private Integer score;

}