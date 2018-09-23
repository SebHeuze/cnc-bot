package org.cnc.cncbot.stats.dao;

import java.util.Date;

import org.cnc.cncbot.stats.entities.StatsPoi;
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
public interface StatsPoiDAO extends JpaRepository<StatsPoi, Long> {

	@Query(value = "TRUNCATE TABLE poi", nativeQuery = true)
	@Transactional
	@Modifying
	public void truncateTable();
	
	@Query(value = "INSERT INTO poi_hist (id,id_alliance,level,type,coord_x,coord_y,date) SELECT poi.*, ?1 FROM poi", nativeQuery = true)
	@Transactional
	@Modifying
	public void archive(Date dateArchive);
}
