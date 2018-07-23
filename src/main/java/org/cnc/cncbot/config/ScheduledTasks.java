package org.cnc.cncbot.config;

import java.util.Date;

import org.cnc.cncbot.map.service.MapService;
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
	
	@Autowired
	public ScheduledTasks (MapService mapService) {
		this.mapService = mapService;
	}

    @Scheduled(fixedRateString = "${cncmap.batch1.fixedRate}")
    public void batch1() {
        log.info("Map batch num 1 started at {}", new Date());
        this.mapService.mapDataJob(1);
    }
    
    @Scheduled(fixedRateString = "${cncmap.batch5.fixedRate}")
    public void batch5() {
        log.info("Map batch num 5 started at {}", new Date());
        this.mapService.mapDataJob(5);
    }

    @Scheduled(fixedRateString = "${cncmap.batch10.fixedRate}")
    public void batch10() {
        log.info("Map batch num 10 started at {}", new Date());
        this.mapService.mapDataJob(10);
    }

    @Scheduled(fixedRateString = "${cncmap.batch20.fixedRate}")
    public void batch20() {
        log.info("Map batch num 20 started at {}", new Date());
        this.mapService.mapDataJob(20);
    }

    @Scheduled(fixedRateString = "${cncmap.batch60.fixedRate}")
    public void batch60() {
        log.info("Map batch num 60 started at {}", new Date());
        this.mapService.mapDataJob(60);
    }
}