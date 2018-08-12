package org.cnc.cncbot.stats.dao;

import java.util.List;

import org.cnc.cncbot.stats.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for Account
 * @author heuze
 *
 */
@Repository
public interface AccountDAO extends JpaRepository<Account, Integer> {

	public List<Account> findAll();

}
