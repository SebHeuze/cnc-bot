package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.PlayerHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for PlayerHist
 * @author heuze
 *
 */
@Repository
public interface PlayerHistDAO extends JpaRepository<PlayerHist, Long> {

}
