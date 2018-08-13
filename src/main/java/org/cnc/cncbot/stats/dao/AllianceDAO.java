package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.Alliance;
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
@Repository
public interface AllianceDAO extends JpaRepository<Alliance, Long> {

	@Query(value = "TRUNCATE TABLE alliance", nativeQuery = true)
	@Transactional
	@Modifying
	public void truncateTable();
}
