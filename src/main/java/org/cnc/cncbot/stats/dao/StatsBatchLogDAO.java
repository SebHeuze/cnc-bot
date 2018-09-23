package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.StatsBatchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for BatchLog
 * @author heuze
 *
 */
@Repository
public interface StatsBatchLogDAO extends JpaRepository<StatsBatchLog, Integer> {

}
