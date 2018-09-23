package org.cnc.cncbot.stats.dao;

import java.util.List;

import org.cnc.cncbot.stats.entities.StatsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for Account
 * @author heuze
 *
 */
@Repository
public interface StatsAccountDAO extends JpaRepository<StatsAccount, Integer> {

	public List<StatsAccount> findAll();

	@Modifying
	@Transactional
	@Query("UPDATE StatsAccount a set a.playersCount = ?2, a.alliancesCount = ?3 WHERE a = ?1")
	void updateCounts(StatsAccount account, int playersCount, int alliancesCount);
}