package org.cnc.cncbot.map.dao;


import org.cnc.cncbot.map.entities.Alliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for Alliance
 * @author heuze
 *
 */
@Transactional
@Repository
public interface AllianceDAO  extends JpaRepository<Alliance, Long> {
	@Modifying
	@Query("UPDATE Alliance a set a.nbPlayers = (SELECT COUNT(1) FROM Player p WHERE p.allianceId = a.allianceId)")
	void updateNbJoueurs();

	@Query(value = "TRUNCATE TABLE alliance", nativeQuery = true)
	@Modifying
	public void truncateTable();
}
