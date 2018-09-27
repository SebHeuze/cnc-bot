package org.cnc.cncbot.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class SchemaPerWorldConnectionProviderH2 extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl  {

	/**
	 * Generated Serial
	 */
	private static final long serialVersionUID = -5469717413476068582L;

	@Autowired
	private Map<String, DataSource> dataSourcesCncBot;

	@Override
	protected DataSource selectAnyDataSource() {
		return this.dataSourcesCncBot.values().iterator().next();
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		return this.dataSourcesCncBot.get(tenantIdentifier);
	}


	@Override
	public Connection getAnyConnection() throws SQLException {
		return this.selectAnyDataSource().getConnection();
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		connection.close();
	}

	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
		String[] datasourceInfos = tenantIdentifier.split("\\.");
		final Connection connection = this.dataSourcesCncBot.get(datasourceInfos[0]).getConnection();
		try {
			connection.createStatement().execute(String.format("SET SCHEMA %s", datasourceInfos[1]));
		} catch (SQLException e) {
			throw new HibernateException("Could not alter JDBC connection to specified schema [" + datasourceInfos[1] + "]",
					e);
		}
		return connection;
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		try {
			connection.createStatement().execute(String.format("SET SCHEMA %s", DBContext.defaultSchema ));
		} catch (SQLException e) {
			throw new HibernateException("Could not alter JDBC connection to specified schema [" + DBContext.defaultSchema + "]",
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