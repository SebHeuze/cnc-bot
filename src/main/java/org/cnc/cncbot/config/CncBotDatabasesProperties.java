package org.cnc.cncbot.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "cncbot.jpa")
@Getter
@Setter
public class CncBotDatabasesProperties {

	private List<DataSourceProperties> datasources;

	@Getter
	@Setter
	public static class DataSourceProperties extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties {

		private String app;
	}
}