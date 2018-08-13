package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for Base
 * @author heuze
 *
 */
@Repository
public interface BaseDAO extends JpaRepository<Base, Long> {

	@Query(value = "TRUNCATE TABLE base", nativeQuery = true)
	@Transactional
	@Modifying
	public void truncateTable();
}
