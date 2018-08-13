package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.Alliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for Alliance
 * @author heuze
 *
 */
@Repository
public interface AllianceDAO extends JpaRepository<Alliance, Long> {

}
