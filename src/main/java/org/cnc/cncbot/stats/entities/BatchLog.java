package org.cnc.cncbot.stats.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * BatchLog entity
 * @author heuze
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stats_batch_log", schema = "scripting")
public class BatchLog {
	@Id
	@GeneratedValue
	private Long id;
	
	private Integer numBatch;
	
	@Column(columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateDebut;
	
	@Column(columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateFin;
	
	private Long duree;
	
	private Integer nbWorlds;
	
	private Integer nbWorldsFails;
	
	private String failList;
}