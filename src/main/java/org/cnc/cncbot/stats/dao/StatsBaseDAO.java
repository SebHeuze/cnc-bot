package org.cnc.cncbot.stats.dao;

import java.util.Date;

import org.cnc.cncbot.stats.entities.StatsBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for Base
 * @author heuze
 *
 */
@Repository
public interface StatsBaseDAO extends JpaRepository<StatsBase, Long> {

	@Query(value = "TRUNCATE TABLE stats_base", nativeQuery = true)
	@Transactional(propagation=Propagation.MANDATORY)
	@Modifying
	public void truncateTable();
	
	@Query(value = "INSERT INTO stats_base_hist (id, id_joueur, nom_base, score_base,coord_x,coord_y,date) SELECT stats_base.*, ?1 FROM stats_base", nativeQuery = true)
	@Transactional(propagation=Propagation.MANDATORY)
	@Modifying
	public void archive(Date dateArchive);
}
