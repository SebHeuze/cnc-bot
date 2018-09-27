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
 * The persistent class for the alliance_hist database table.
 * 
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="stats_alliance_hist")
public class StatsAllianceHist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator (name = "stats_base_hist_id_hist", sequenceName = "stats_base_hist_id_hist_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stats_base_hist_id_hist")
	@Column(name="id_hist")
	private Long idHist;

	private Long id;

	@Column(name="alliance_description")
	private String allianceDescription;

	@Column(name="average_score")
	private Integer averageScore;

	@Column(name="bases_joueurs_detruites")
	private Integer destroyedPlayersBases;

	@Column(name="bases_oublies_detruites")
	private Integer destroyedForgottenBases;

	@Column(name="total_bases_detruites")
	private Integer totalDestroyedBases;

	@Column(name="distance_centre")
	private Integer distanceCentre;

	@Column(name="nb_poi")
	private Integer nbPoi;

	@Column(name="nom_alliance")
	private String allianceName;

	@Column(name="nombre_bases")
	private Integer basesCount;

	@Column(name="nombre_joueurs")
	private Integer playersCount;

	@Column(name="rang")
	private Integer rank;

	@Column(name="rank_poi_1")
	private Integer rankPoi1;

	@Column(name="rank_poi_2")
	private Integer rankPoi2;

	@Column(name="rank_poi_3")
	private Integer rankPoi3;

	@Column(name="rank_poi_4")
	private Integer rankPoi4;

	@Column(name="rank_poi_5")
	private Integer rankPoi5;

	@Column(name="rank_poi_6")
	private Integer rankPoi6;

	@Column(name="rank_poi_7")
	private Integer rankPoi7;

	private Long score;

	@Column(name="score_poi_1")
	private Integer scorePoi1;

	@Column(name="score_poi_2")
	private Integer scorePoi2;

	@Column(name="score_poi_3")
	private Integer scorePoi3;

	@Column(name="score_poi_4")
	private Integer scorePoi4;

	@Column(name="score_poi_5")
	private Integer scorePoi5;

	@Column(name="score_poi_6")
	private Integer scorePoi6;

	@Column(name="score_poi_7")
	private Integer scorePoi7;

	@Column(name="top_score")
	private Long topScore;
	
	@Temporal(TemporalType.DATE)
	private Date date;

}