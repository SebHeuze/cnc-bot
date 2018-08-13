package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for Stat
 * @author heuze
 *
 */
@Repository
public interface StatsDAO extends JpaRepository<Stat, Long> {

}
