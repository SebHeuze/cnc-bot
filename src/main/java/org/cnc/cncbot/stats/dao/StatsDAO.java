package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for Stat
 * @author heuze
 *
 */
@Repository
public interface StatsDAO extends JpaRepository<Stat, Long> {

	@Query(value = "TRUNCATE TABLE stats", nativeQuery = true)
	@Transactional(propagation=Propagation.REQUIRED)
	@Modifying
	public void truncateTable();
}
