package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.BatchLog;
import org.cnc.cncbot.stats.entities.StatsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for BatchLog
 * @author heuze
 *
 */
@Repository
public interface StatsLogDAO extends JpaRepository<StatsLog, Integer> {

}
