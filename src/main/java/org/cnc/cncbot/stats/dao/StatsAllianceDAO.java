package org.cnc.cncbot.stats.dao;

import java.util.Date;

import org.cnc.cncbot.stats.entities.StatsAlliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for Alliance
 * @author heuze
 *
 */
@Repository
public interface StatsAllianceDAO extends JpaRepository<StatsAlliance, Long> {

	@Query(value = "TRUNCATE TABLE stats_alliance", nativeQuery = true)
	@Transactional(propagation=Propagation.MANDATORY)
	@Modifying
	public void truncateTable();
	

	@Query(value = "INSERT INTO stats_alliance_hist (id,nom_alliance,nombre_bases,nombre_joueurs,rang,score,top_score,average_score,total_bases_detruites,bases_oublies_detruites,bases_joueurs_detruites,distance_centre,alliance_description,nb_poi,rank_poi_1,rank_poi_2,rank_poi_3,rank_poi_4,rank_poi_5,rank_poi_6,rank_poi_7,score_poi_1,score_poi_2,score_poi_3,score_poi_4,score_poi_5,score_poi_6,score_poi_7,date) SELECT stats_alliance.*, ?1 FROM stats_alliance", nativeQuery = true)
	@Transactional(propagation=Propagation.MANDATORY)
	@Modifying
	public void archive(Date dateArchive);
}
