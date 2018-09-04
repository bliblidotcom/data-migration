package com.gdn.data.migration.postgre;

import com.gdn.data.migration.core.DataMigrationAutoConfigurer;
import com.gdn.data.migration.core.DataMigrationProperties;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Eko Kurniawan Khannedy
 */
@Configuration
@AutoConfigureAfter(DataMigrationAutoConfigurer.class)
@EnableConfigurationProperties({PostgreConfiguration.class, DataMigrationProperties.class})
public class PostgreAutoConfigurer {

  /**
   * Postgre Datasource bean
   *
   * @return postgre datasource
   */
  @Bean
  @Autowired
  public PGPoolingDataSource pgPoolingDataSource(PostgreConfiguration configuration) {
    PGPoolingDataSource dataSource = new PGPoolingDataSource();
    dataSource.setUser(configuration.getUsername());
    dataSource.setPassword(configuration.getPassword());
    dataSource.setServerName(configuration.getHost());
    dataSource.setPortNumber(configuration.getPort());
    dataSource.setDatabaseName(configuration.getDatabase());
    return dataSource;
  }

  /**
   * PostgreService bean
   *
   * @return PostgreService
   */
  @Bean
  @Autowired
  public PostgreService postgreService(PGPoolingDataSource dataSource) {
    DSLContext context = DSL.using(dataSource, SQLDialect.POSTGRES);
    return new PostgreService(context);
  }

  /**
   * PostgreInternal
   *
   * @param postgreService postgre service
   * @return PostgreInternal
   */
  @Bean
  @Autowired
  public PostgreInternal postgreInternal(PostgreService postgreService, DataMigrationProperties dataMigrationProperties) {
    return new PostgreInternal(postgreService, dataMigrationProperties);
  }

}
