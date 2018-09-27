package org.cnc.cncbot.config;

import java.util.Date;

import org.cnc.cncbot.service.MapService;
import org.cnc.cncbot.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Class for Scheduled batchs
 * @author sheuze
 *
 */
@Component
@Slf4j
public class ScheduledTasks {

	private final MapService mapService;
	
	private final StatsService statsService;
	
	@Autowired
	public ScheduledTasks (MapService mapService, StatsService statsService) {
		this.mapService = mapService;
		this.statsService = statsService;
	}


    /**
     * STATS SHEDULER
     */
    @Scheduled(initialDelayString = "${cncbot.stats.batch.initialDelay}",  fixedRateString = "${cncbot.stats.batch.fixedRate}")
    public void statsScheduler() {
        log.info("Stats batch started at {}", new Date());
        this.statsService.statsJob();
    }
    /**
     * MAP SCHEDULER
     */
    @Scheduled(initialDelayString = "${cncbot.map.batch1.initialDelay}" ,fixedDelayString = "${cncbot.map.batch1.fixedDelay}")
    public void batch1() {
        log.info("Map batch num 1 started at {}", new Date());
        this.mapService.mapDataJob(1);
    }
    
    @Scheduled(initialDelayString = "${cncbot.map.batch5.initialDelay}" ,fixedDelayString = "${cncbot.map.batch5.fixedDelay}")
    public void batch5() {
        log.info("Map batch num 5 started at {}", new Date());
        this.mapService.mapDataJob(5);
    }

    @Scheduled(initialDelayString = "${cncbot.map.batch10.initialDelay}" ,fixedDelayString = "${cncbot.map.batch10.fixedDelay}")
    public void batch10() {
        log.info("Map batch num 10 started at {}", new Date());
        this.mapService.mapDataJob(10);
    }

    @Scheduled(initialDelayString = "${cncbot.map.batch20.initialDelay}" ,fixedDelayString = "${cncbot.map.batch20.fixedDelay}")
    public void batch20() {
        log.info("Map batch num 20 started at {}", new Date());
        this.mapService.mapDataJob(20);
    }

    @Scheduled(initialDelayString = "${cncbot.map.batch60.initialDelay}" ,fixedDelayString = "${cncbot.map.batch60.fixedDelay}")
    public void batch60() {
        log.info("Map batch num 60 started at {}", new Date());
        this.mapService.mapDataJob(60);
    }
    
}