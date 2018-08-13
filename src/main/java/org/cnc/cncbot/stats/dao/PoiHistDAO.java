package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.PoiHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for PoiHist
 * @author heuze
 *
 */
@Repository
public interface PoiHistDAO extends JpaRepository<PoiHist, Long> {

}
