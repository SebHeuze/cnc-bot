package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for Player
 * @author heuze
 *
 */
@Repository
public interface PlayerDAO extends JpaRepository<Player, Long> {

}
