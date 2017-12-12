package com.gdn.data.migration.mongo;

import java.net.UnknownHostException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.gdn.data.migration.core.DataMigrationAutoConfigurer;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

/**
 * @author Eko Kurniawan Khannedy
 */
@Configuration
@AutoConfigureAfter(DataMigrationAutoConfigurer.class)
@EnableConfigurationProperties(MongoConfiguration.class)
public class MongoAutoConfigurer {

  /**
   * MongoClient bean
   *
   * @return MongoClient
   */
  @Bean
  @Autowired
  public MongoClient mongoClient(MongoConfiguration configuration) {
    if (configuration.getUsername() != null) {
      // using authentication
      return new MongoClient(new ServerAddress(configuration.getHost(), configuration.getPort()),
          Collections.singletonList(MongoCredential.createCredential(configuration.getUsername(),
              configuration.getDatabase(), configuration.getPassword().toCharArray())));
    } else {
      // not using authentication
      return new MongoClient(configuration.getHost(), configuration.getPort());
    }
  }

  /**
   * MongoDatabase bean
   *
   * @param mongoClient mongo client
   * @return MongoDatabase
   */
  @Bean
  @Autowired
  public MongoDatabase mongoDatabase(MongoConfiguration configuration, MongoClient mongoClient) {
    return mongoClient.getDatabase(configuration.getDatabase());
  }

  /**
   * MongoDBFactory bean
   *
   * @param mongo
   * @param configuration
   * @return
   * @throws Exception
   */
  @Bean
  @Autowired
  public SimpleMongoDbFactory mongoDbFactory(MongoClient mongo, MongoConfiguration configuration)
      throws Exception {
    String database = configuration.getDatabase();
    return new SimpleMongoDbFactory(mongo, database);
  }

  /**
   * MongoInternal bean
   *
   * @param mongoDatabase mongo database
   * @return MongoInternal
   */
  @Bean
  @Autowired
  public MongoInternal mongoInternal(MongoDatabase mongoDatabase) {
    return new MongoInternal(mongoDatabase);
  }

  /**
   * MongoTemplate bean, use it to insert entity instead of basic document
   *
   * @param mongoDbFactory
   * @return
   * @throws UnknownHostException
   */
  @Bean
  @Autowired
  public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) throws UnknownHostException {
    return new MongoTemplate(mongoDbFactory);
  }
}
