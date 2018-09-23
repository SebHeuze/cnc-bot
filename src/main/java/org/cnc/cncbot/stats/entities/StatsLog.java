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
 * StatLog entity
 * @author heuze
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stats_log", schema = "scripting")
public class StatsLog {
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "id_stat")
	private Long statId;

	@Column(name = "duree")
	private Integer time;
	
	@Column(columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name = "monde")
	private Integer world;
	
	@Column(name = "alliance")
	private Long alliance;
	
}