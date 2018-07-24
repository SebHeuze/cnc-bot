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
 * @author sheuze
 *
 */
@EnableScheduling
@SpringBootApplication(
		exclude = { DataSourceAutoConfiguration.class } //, HibernateJpaAutoConfiguration.class
		)
@EnableAsync
public class CncbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(CncbotApplication.class, args);
	}
	
	@Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(40);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("CncBot-");
        executor.initialize();
        return executor;
    }
}
