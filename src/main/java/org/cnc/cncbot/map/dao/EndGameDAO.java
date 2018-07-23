package org.cnc.cncbot.map.dao;


import org.cnc.cncbot.map.entities.Coords;
import org.cnc.cncbot.map.entities.EndGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Class for EndGame
 * @author heuze
 *
 */
@Transactional
@Repository
public interface EndGameDAO  extends JpaRepository<EndGame, Coords> {

}
