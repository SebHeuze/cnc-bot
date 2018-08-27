package org.cnc.cncbot.stats.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "stats_list", schema = "scripting")
public class StatsList {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "nom")
	private String name;

	@Column(name = "requete")
	private String request;

	@Column(name = "type")
	private Integer type;
}