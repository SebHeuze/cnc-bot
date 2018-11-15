package org.cnc.cncbot.map.entities;

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
@Table(name = "liste_comptes", schema = "scripting")
public class Account {
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name="`user`")
	private String user;
	private String  pass;
	
	@Column(name="monde")
	private Integer worldId;
	private Boolean active;
	private Integer numbatch;
}