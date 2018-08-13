package org.cnc.cncbot.stats.dao;

import java.util.Date;

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
	
	@Query(value = "INSERT INTO base_hist (id, id_joueur, nom_base, score_base,coord_x,coord_y,date) SELECT base.*, ?1 FROM base", nativeQuery = true)
	@Transactional
	@Modifying
	public void archive(Date dateArchive);
}
