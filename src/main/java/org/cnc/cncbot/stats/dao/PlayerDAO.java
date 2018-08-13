package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for Player
 * @author heuze
 *
 */
@Repository
public interface PlayerDAO extends JpaRepository<Player, Long> {

	@Query(value = "TRUNCATE TABLE joueur", nativeQuery = true)
	@Transactional
	@Modifying
	public void truncateTable();
}
