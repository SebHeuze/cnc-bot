package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.BaseHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for BaseHist
 * @author heuze
 *
 */
@Repository
public interface BaseHistDAO extends JpaRepository<BaseHist, Long> {

}
