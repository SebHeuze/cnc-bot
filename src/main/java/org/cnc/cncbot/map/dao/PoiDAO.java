package org.cnc.cncbot.map.dao;


import org.cnc.cncbot.map.entities.Coords;
import org.cnc.cncbot.map.entities.Poi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for POI
 * @author heuze
 *
 */
@Repository
public interface PoiDAO  extends JpaRepository<Poi, Coords> {

	@Query(value = "TRUNCATE TABLE poi", nativeQuery = true)
	@Transactional
	@Modifying
	public void truncateTable();
}
