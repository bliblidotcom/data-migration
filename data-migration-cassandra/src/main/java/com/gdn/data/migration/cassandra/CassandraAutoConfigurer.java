package com.gdn.data.migration.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.gdn.data.migration.core.DataMigrationAutoConfigurer;
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
@EnableConfigurationProperties(CassandraConfiguration.class)
public class CassandraAutoConfigurer {

  /**
   * Cassandra Cluster bean
   *
   * @param configuration configuration
   * @return Cluster
   */
  @Bean
  @Autowired
  public Cluster cluster(CassandraConfiguration configuration) {
    Cluster.Builder builder = Cluster.builder().addContactPoint(configuration.getHost());

    if (configuration.getPort() != null) {
      builder = builder.withPort(configuration.getPort());
    }

    if (configuration.getUsername() != null) {
      builder = builder.withCredentials(configuration.getUsername(), configuration.getPassword());
    }

    return builder.build();
  }

  /**
   * Cassandra Session
   *
   * @param cluster cluster
   * @return Session
   */
  @Bean
  @Autowired
  public Session session(CassandraConfiguration configuration, Cluster cluster) {
    return cluster.connect(configuration.getKeyspace());
  }

  /**
   * CassandraInternal
   *
   * @param session session
   * @return CassandraInternal
   */
  @Bean
  @Autowired
  public CassandraInternal cassandraInternal(Session session) {
    return new CassandraInternal(session);
  }

}
