package org.cnc.cncbot.map.dao;


import org.cnc.cncbot.map.entities.Poi;
import org.cnc.cncbot.map.entities.PoiId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for POI
 * @author heuze
 *
 */
@Transactional
@Repository
public interface PoiDAO  extends JpaRepository<Poi, PoiId> {

}
