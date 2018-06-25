package org.cnc.cncbot.map.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


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
public class Account {
	@Id
	@GeneratedValue
	private Integer id;
	private Integer user;
	private String  pass;
	private Integer monde;
	private Integer active;
	private Integer numbatch;
}