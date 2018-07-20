package org.cnc.cncbot.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.AbstractDataSource;


public class DynamicSchemaRoutingDatasource extends AbstractDataSource {

	@Value("${spring.datasource.username}")
	private String datasourceUser;

	@Value("${spring.datasource.password}")
	private String datasourcePass;

	@Value("${spring.datasource.defaultSchema}")
	private String defaultSchema;
	
	@Value("${spring.datasource.url}")
	private String datasourceUrl;
	
	@Value("${spring.datasource.driverClassName}")
	private String datasourceDriverClassName;

    private Map<String, DataSource> dataSources = new HashMap<>();
    
    @Override
    public Connection getConnection() throws SQLException {
        return determineTargetDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineTargetDataSource().getConnection(username, password);
    }

    private DataSource determineTargetDataSource() {
        String schema = DBContext.getSchema() != null ? DBContext.getSchema() : defaultSchema;
        
        if (!dataSources.containsKey(schema)) {
        	dataSources.put(schema, buildDataSourceForSchema(schema));
        }
        return dataSources.get(schema);
    }

    private DataSource buildDataSourceForSchema(String schema) {
        String url = datasourceUrl + schema;
        return DataSourceBuilder.create()
            .driverClassName(datasourceDriverClassName)
            .username(datasourceUser)
            .password(datasourcePass)
            .url(url)
            .build();
    }
}
