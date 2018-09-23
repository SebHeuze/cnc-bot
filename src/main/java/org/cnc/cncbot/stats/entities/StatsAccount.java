package org.cnc.cncbot.stats.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Account entity
 * @author heuze
 *
 */
@Entity
@Getter
@Setter
@Table(name = "stats_liste_comptes", schema = "scripting")
public class StatsAccount {
	@Id
	@GeneratedValue
	private Integer id;
	private String user;
	private String  pass;
	
	@Column(name="monde")
	private Integer worldId;
	private Boolean active;
	private String timezone;
	
	@Column(name="nb_joueurs")
	private Integer playersCount;	
	
	@Column(name="nb_alliances")
	private Integer alliancesCount;
}