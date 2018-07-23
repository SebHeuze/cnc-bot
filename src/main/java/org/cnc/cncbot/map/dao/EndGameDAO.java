package org.cnc.cncbot.map.dao;


import org.cnc.cncbot.map.entities.Coords;
import org.cnc.cncbot.map.entities.EndGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for EndGame
 * @author heuze
 *
 */
@Transactional
@Repository
public interface EndGameDAO  extends JpaRepository<EndGame, Coords> {

	@Query(value = "TRUNCATE TABLE endgame", nativeQuery = true)
	@Modifying
	public void truncateTable();
}
