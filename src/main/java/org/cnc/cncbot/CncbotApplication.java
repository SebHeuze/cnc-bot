package org.cnc.cncbot;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Main Class of CNCBOT
 * @author SebHeuze
 *
 */
@EnableScheduling
@SpringBootApplication(
		exclude = { DataSourceAutoConfiguration.class } //, HibernateJpaAutoConfiguration.class
		)
//@EnableTransactionManagement(mode=AdviceMode.ASPECTJ)
public class CncbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(CncbotApplication.class, args);
	}
	
}
