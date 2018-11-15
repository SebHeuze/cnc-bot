package org.cnc.cncbot.map.dao;


import java.util.List;

import org.cnc.cncbot.map.entities.Coords;
import org.cnc.cncbot.map.entities.Poi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for POI
 * @author SebHeuze
 *
 */
@Repository
public interface PoiDAO  extends JpaRepository<Poi, Coords> {

	/**
	 * Force new transaction since this method is used in method using stats Transaction
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<Poi> findAll();
	
	@Query(value = "TRUNCATE TABLE poi", nativeQuery = true)
	@Transactional
	@Modifying
	public void truncateTable();
}
