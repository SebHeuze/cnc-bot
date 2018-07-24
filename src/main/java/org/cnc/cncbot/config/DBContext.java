package org.cnc.cncbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DBContext {

	public static String defaultSchema;
	
	public static String defaultDatasource;

	private static final ThreadLocal<String> SCHEMA = new ThreadLocal<>();
	private static final ThreadLocal<String> DATASOURCE = new ThreadLocal<>();

	public static void setSchema(String schema) {
		SCHEMA.set(schema);
	}

	public static String getSchema() {
		return SCHEMA.get() != null ? SCHEMA.get() : defaultSchema;
	}

	public static void setDatasource(String datasource) {
		DATASOURCE.set(datasource);
	}

	public static String getDatasource() {
		return DATASOURCE.get() != null ? DATASOURCE.get() : defaultDatasource;
	}
	
	@Value("${spring.datasource.defaultSchema:public}")
    public void setDefaultSchema(String defaultSchema) {
		DBContext.defaultSchema = defaultSchema;
    }
	
	@Value("${spring.datasource.defaultDatasource:cncmap}")
    public void setDefaultDataSource(String defaultDatasource) {
		DBContext.defaultDatasource = defaultDatasource;
    }

}
