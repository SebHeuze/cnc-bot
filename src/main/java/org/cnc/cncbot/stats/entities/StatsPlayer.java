package org.cnc.cncbot.stats.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

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
public class StatsPlayer implements Serializable, Persistable<Integer> {
	private static final long serialVersionUID = 1L;

	@Id
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
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="id_joueur")
	private List<StatsBase> bases;

	/**
	 * We are doing to stop hibernate from doing select to check if exist before insert
	 * Perfomance improvement since we delete all result before insert
	 */
	@Override
	public boolean isNew() {
		return true;
	}
	
}