package org.cnc.cncbot.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.cnc.cncbot.CncbotApplication;
import org.cnc.cncbot.config.CncBotDatabasesProperties.DataSourceProperties;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableConfigurationProperties({CncBotDatabasesProperties.class, JpaProperties.class})
@EnableJpaRepositories(basePackages = { "org.cnc.cncbot" }, transactionManagerRef = "txManager")
@EnableTransactionManagement
public class SpringJpaConfiguration {

  @Autowired
  private JpaProperties jpaProperties;

  @Autowired
  @Qualifier("cncBotDatabasesProperties")
  private CncBotDatabasesProperties databasesProperties;
  
  @Bean(name = "dataSourcesCncBot" )
  public Map<String, DataSource> dataSourcesCncBot() {
    Map<String, DataSource> result = new HashMap<>();
    for (DataSourceProperties dsProperties : this.databasesProperties.getDatasources()) {
      DataSourceBuilder<HikariDataSource> factory = DataSourceBuilder
        .create()
        .url(dsProperties.getUrl())
        .username(dsProperties.getUsername())
        .password(dsProperties.getPassword())
        .type(HikariDataSource.class)
        .driverClassName(dsProperties.getDriverClassName());
      result.put(dsProperties.getApp(), factory.build());
    }
    return result;
  }

  @Bean
  public MultiTenantConnectionProvider multiTenantConnectionProvider() {
    // Autowires dataSourcesDvdRental
    return new SchemaPerWorldConnectionProviderH2();
  }

  @Bean
  public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
    return new CurrentWorldSchemaIdentifierResolver();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(MultiTenantConnectionProvider multiTenantConnectionProvider,
    CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

    Map<String, Object> hibernateProps = new LinkedHashMap<>();
    hibernateProps.putAll(this.jpaProperties.getProperties());
    hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
    hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
    hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

    // No dataSource is set to resulting entityManagerFactoryBean
    LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
    result.setPackagesToScan(new String[] { CncbotApplication.class.getPackage().getName() });
    result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    result.setJpaPropertyMap(hibernateProps);

    return result;
  }
  @Bean
  public PlatformTransactionManager txManager(EntityManagerFactory entityManagerFactory) {
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(entityManagerFactory);
	    return transactionManager;
  }
}