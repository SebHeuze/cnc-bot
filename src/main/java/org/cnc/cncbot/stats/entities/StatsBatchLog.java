package org.cnc.cncbot.stats.entities;

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
 * BatchLog entity
 * @author SebHeuze
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stats_batch_log", schema = "scripting")
public class StatsBatchLog {
	@Id
	@SequenceGenerator (name = "stats_batch_log_id", sequenceName = "scripting.stats_batch_log_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stats_batch_log_id")
	private Long id;
	
	@Column(name="num_batch")
	private Integer numBatch;
	
	@Column(name="date_debut", columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateDebut;
	
	@Column(name="date_fin", columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateFin;
	
	private Long duree;

	@Column(name="nb_worlds")
	private Integer nbWorlds;

	@Column(name="nb_worlds_fails")
	private Integer nbWorldsFails;

	@Column(name="fail_list")
	private String failList;
}