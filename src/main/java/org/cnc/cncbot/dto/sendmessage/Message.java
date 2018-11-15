package org.cnc.cncbot.dto.sendmessage;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Objet Message.
 * @author SebHeuze
 *
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    
    /**
     * Id
     */
    private int id;
    
    /**
     * pseudo destinataire.
     */
    private String pseudo;
    
    /**
     * titre du message.
     */
    private String titre;
    
    /**
     * message.
     */
    private String message;
    
    /**
     * id Monde.
     */
    private int monde;
    
    /**
     * envoye oui ou non.
     */
    private int envoye;
    
    /**
     * date insere.
     */
    private Date dateInsere;
    
    /**
     * date envoye.
     */
    private Date dateEnvoye;
    
}
