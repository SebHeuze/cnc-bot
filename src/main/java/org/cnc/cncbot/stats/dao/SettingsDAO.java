package org.cnc.cncbot.stats.dao;


import org.cnc.cncbot.stats.entities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for Settings
 * @author heuze
 *
 */
@Repository
public interface SettingsDAO  extends JpaRepository<Settings, String> {

}
