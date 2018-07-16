package org.cnc.cncbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Class of CNCBOT
 * @author sheuze
 *
 */
@EnableScheduling
@SpringBootApplication
public class CncbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(CncbotApplication.class, args);
	}
}
