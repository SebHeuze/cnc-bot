package org.cnc.cncbot.stats.dao;


import org.cnc.cncbot.stats.entities.StatsSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for Settings
 * @author heuze
 *
 */
@Repository
public interface StatsSettingsDAO  extends JpaRepository<StatsSettings, String> {

	@Modifying
	@Transactional(propagation=Propagation.MANDATORY)
	@Query("UPDATE StatsSettings s set s.value = ?2 WHERE s.name = ?1")
	public void updateSetting(String name, String value);
}
