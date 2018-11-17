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

import lombok.Getter;
import lombok.Setter;

/**
 * Message entity
 * @author SebHeuze
 *
 */
@Entity
@Getter
@Setter
@Table(name = "messages", schema = "scripting")
public class Message {
	@Id
	@SequenceGenerator (name = "messages_id", sequenceName = "messages_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messages_id")
	private Integer	id;	

	private String pseudo;

	private String message;

	private Integer monde;

	private Integer envoye;

	private String  titre;
	
	@Column(name="date_insere", columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	Date dateInsere;
	
	@Column(name="date_envoye", columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	Date dateEnvoye;

	private Integer fails;
}