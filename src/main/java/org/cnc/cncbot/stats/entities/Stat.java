package org.cnc.cncbot.stats.entities;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the stats database table.
 * 
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="stats")
public class Stat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="donnees_stat")
	private String data;

	@Column(name="id_alliance")
	private Integer idAlliance;

	@Column(name="nom_stat")
	private String name;

}