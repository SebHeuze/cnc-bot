package org.cnc.cncbot.stats.dao;

import java.util.List;

import org.cnc.cncbot.stats.entities.StatsList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO Class for StatsList
 * @author heuze
 *
 */
@Repository
public interface StatsListDAO extends JpaRepository<StatsList, Long> {

	public List<StatsList> findAllByType(Integer type);
}
