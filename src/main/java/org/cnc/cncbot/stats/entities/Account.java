package org.cnc.cncbot.stats.entities;

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
@Table(name = "liste_comptes", schema = "scripting")
public class Account {
	@Id
	@GeneratedValue
	private Integer id;
	private String user;
	private String  pass;
	private Integer monde;
	private Boolean active;
	private Integer numbatch;
}