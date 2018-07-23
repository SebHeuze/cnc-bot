package org.cnc.cncbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DBContext {

	public static String defaultSchema;

	private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

	public static void setSchema(String schema) {
		CONTEXT.set(schema);
	}

	public static String getSchema() {
		return CONTEXT.get() != null ? CONTEXT.get() : defaultSchema;
	}

	@Value("${spring.datasource.defaultSchema}")
    public void setDefaultSchema(String ds) {
		defaultSchema = ds;
    }

}
