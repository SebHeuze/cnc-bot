package org.cnc.cncbot.stats.dao;

import java.util.Date;
import java.util.List;

import org.cnc.cncbot.stats.entities.StatsAlliance;
import org.cnc.cncbot.stats.entities.StatsPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for Player
 * @author SebHeuze
 *
 */
@Repository
public interface StatsPlayerDAO extends JpaRepository<StatsPlayer, Long> {

	@Query(value = "TRUNCATE TABLE stats_joueur", nativeQuery = true)
	@Transactional(propagation=Propagation.MANDATORY)
	@Modifying
	public void truncateTable();
	

	@Query(value = "INSERT INTO stats_joueur_hist (id,pseudo,faction,rang,score,id_alliance,total_bases_detruites,bases_oublies_detruites,bases_joueurs_detruites,distance_centre,nb_tacitus,date) SELECT stats_joueur.*, ?1 FROM stats_joueur", nativeQuery = true)
	@Transactional(propagation=Propagation.MANDATORY)
	@Modifying
	public void archive(Date dateArchive);
	
	@Query(value = "SELECT DISTINCT new StatsAlliance(idAlliance) FROM StatsPlayer  WHERE id IN ?1 AND idAlliance<>0")
	public List<StatsAlliance> findAlliancesOfPlayers(List<Integer> playerIds);
}
