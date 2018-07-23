package org.cnc.cncbot.map.dao;


import org.cnc.cncbot.map.entities.Player;
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
public interface PlayerDAO  extends JpaRepository<Player, Integer> {

	@Query(value = "TRUNCATE TABLE joueur", nativeQuery = true)
	@Transactional
	@Modifying
	public void truncateTable();
}
