package org.cnc.cncbot.stats.dao;

import java.util.List;

import org.cnc.cncbot.stats.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for Messages
 * @author SebHeuze
 *
 */
@Repository
public interface MessageDAO extends JpaRepository<Message, Integer> {
	public List<Message> findByEnvoyeAndFailsLessThan(int envoye, int maxFails);
	
}
