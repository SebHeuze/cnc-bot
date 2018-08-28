package org.cnc.cncbot.stats.dao;

import java.util.Date;
import java.util.List;

import org.cnc.cncbot.stats.entities.Alliance;
import org.cnc.cncbot.stats.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for Player
 * @author heuze
 *
 */
@Repository
public interface PlayerDAO extends JpaRepository<Player, Long> {

	@Query(value = "TRUNCATE TABLE joueur", nativeQuery = true)
	@Transactional
	@Modifying
	public void truncateTable();
	

	@Query(value = "INSERT INTO joueur_hist (id,pseudo,faction,rang,score,id_alliance,total_bases_detruites,bases_oublies_detruites,bases_joueurs_detruites,distance_centre,nb_tacitus,date) SELECT joueur.*, ?1 FROM joueur", nativeQuery = true)
	@Transactional
	@Modifying
	public void archive(Date dateArchive);
	
	@Query(value = "SELECT DISTINCT idAlliance FROM Player  WHERE id IN ?1 AND idAlliance<>0")
	public List<Alliance> findAlliancesOfPlayers(List<Integer> playerIds);
}
