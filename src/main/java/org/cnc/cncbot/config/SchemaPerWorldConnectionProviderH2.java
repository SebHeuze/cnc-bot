package org.cnc.cncbot.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class SchemaPerWorldConnectionProviderH2 implements MultiTenantConnectionProvider, ServiceRegistryAwareService {

    /**
	 * Generated Serial
	 */
	private static final long serialVersionUID = -5469717413476068582L;

	@Value("${spring.datasource.defaultSchema:public}")
    String defaultSchema;
    
	@Autowired
    private DataSource dataSource;

	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {
	    Map lSettings = serviceRegistry.getService(ConfigurationService.class).getSettings();
	    dataSource = (DataSource) lSettings.get( Environment.DATASOURCE );

	}
	
    @Override
    public Connection getAnyConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        final Connection connection = this.getAnyConnection();
        try {
            connection.createStatement().execute(String.format("SET SCHEMA %s", tenantIdentifier));
        } catch (SQLException e) {
            throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]",
                    e);
        }
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        try {
            connection.createStatement().execute(String.format("SET SCHEMA %s", defaultSchema));
        } catch (SQLException e) {
            throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]",
                    e);
        }

        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}