package org.cnc.cncbot.stats.entities;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


/**
 * The persistent class for the joueur_hist database table.
 * 
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="joueur_hist")
public class PlayerHist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_hist")
	private Integer idHist;

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

	@Temporal(TemporalType.DATE)
	private Date date;

}