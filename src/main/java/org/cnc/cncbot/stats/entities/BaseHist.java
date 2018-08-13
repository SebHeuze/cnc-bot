package org.cnc.cncbot.stats.entities;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


/**
 * The persistent class for the base_hist database table.
 * 
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="base_hist")
public class BaseHist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
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