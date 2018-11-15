package org.cnc.cncbot.map.dao;


import org.cnc.cncbot.map.entities.Base;
import org.cnc.cncbot.map.entities.Coords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for Base
 * @author SebHeuze
 *
 */
@Repository
public interface BaseDAO  extends JpaRepository<Base, Coords> {

	@Query(value = "TRUNCATE TABLE base", nativeQuery = true)
	@Transactional
	@Modifying
	public void truncateTable();
}
