package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.Poi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for Poi
 * @author heuze
 *
 */
@Repository
public interface PoiDAO extends JpaRepository<Poi, Long> {

	@Query(value = "TRUNCATE TABLE poi", nativeQuery = true)
	@Transactional
	@Modifying
	public void truncateTable();
}
