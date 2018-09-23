package org.cnc.cncbot.map.dao;

import java.util.List;

import org.cnc.cncbot.map.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for Account
 * @author heuze
 *
 */
@Repository
public interface AccountDAO  extends JpaRepository<Account, Integer> {

	public List<Account> findAll();

	public List<Account> findByNumbatchAndActiveTrue(Integer numbatch);
	
	public Account findByWorldIdAndActiveTrue(Integer monde);

}
