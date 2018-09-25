package org.cnc.cncbot.stats.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the base_hist database table.
 * 
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="stats_base_hist")
public class StatsBaseHist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator (name = "stats_joueur_hist_id_hist", sequenceName = "stats_joueur_hist_id_hist_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stats_joueur_hist_id_hist")
	@Column(name="id_hist")
	private Long idHist;

	private Long id;

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

	@Temporal(TemporalType.DATE)
	private Date date;

}