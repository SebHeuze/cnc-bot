package org.cnc.cncbot.stats.dao;

import java.util.List;

import org.cnc.cncbot.stats.entities.Account;
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
public interface AccountDAO extends JpaRepository<Account, Integer> {

	public List<Account> findAll();

	@Modifying
	@Transactional
	@Query("UPDATE Account a set a.playersCount = ?2, a.alliancesCount = ?3 WHERE a = ?1")
	void updateCounts(Account account, int playersCount, int alliancesCount);
}
