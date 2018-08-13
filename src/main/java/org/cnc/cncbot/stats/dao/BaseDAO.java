package org.cnc.cncbot.stats.dao;

import org.cnc.cncbot.stats.entities.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for Base
 * @author heuze
 *
 */
@Repository
public interface BaseDAO extends JpaRepository<Base, Long> {

}
