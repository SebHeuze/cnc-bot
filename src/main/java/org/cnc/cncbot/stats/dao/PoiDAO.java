package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.Poi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for Poi
 * @author heuze
 *
 */
@Repository
public interface PoiDAO extends JpaRepository<Poi, Long> {

}
