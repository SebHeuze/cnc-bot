package org.cnc.cncbot.stats.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the joueur database table.
 * 
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="stats_joueur")
public class StatsPlayer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="bases_joueurs_detruites")
	private Integer destroyedPlayersBases;

	@Column(name="bases_oublies_detruites")
	private Integer destroyedForgottenBases;

	@Column(name="distance_centre")
	private Integer distanceCentre;

	private Integer faction;

	@Column(name="id_alliance")
	private Integer idAlliance;
	/**
 	 * Only 1st rank medail
	 */
	@Column(name="nb_tacitus")
	private Integer tacitusCount;

	@Column(name="pseudo")
	private String nickname;

	@Column(name="rang")
	private Integer rank;

	private Long score;

	@Column(name="total_bases_detruites")
	private Integer destroyedTotalBases;
	
	@OneToMany
	private List<StatsBase> bases;

}