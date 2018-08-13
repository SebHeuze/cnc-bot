package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.AllianceHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for AllianceHist
 * @author heuze
 *
 */
@Repository
public interface AllianceHistDAO extends JpaRepository<AllianceHist, Long> {

}
