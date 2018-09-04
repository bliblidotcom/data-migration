package com.gdn.data.migration.mongo;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.gdn.data.migration.core.DataMigrationProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.gdn.data.migration.core.Internal;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


/**
 * @author henry.jonathan
 */
public class MongoAutoConfigurerTest {

  private static final String HOST = "testHost";
  private static final Integer PORT = 12345;
  private static final String USERNAME = "testUsername";
  private static final String PASSWORD = "testPassword";
  private static final String DATABASE = "testDatabase";

  MongoConfiguration configuration;

  @InjectMocks
  private MongoAutoConfigurer mongoAutoConfigurer;

  private DataMigrationProperties dataMigrationProperties = DataMigrationProperties.builder().build();

  @Test
  public void mongoClientWithoutUsernameTest() {
    configuration.setUsername(null);
    MongoClient mongoClient = mongoAutoConfigurer.mongoClient(this.configuration);
    assertNotNull(mongoClient);
  }

  @Test
  public void mongoClientWithUsernameTest() {
    MongoClient mongoClient = mongoAutoConfigurer.mongoClient(this.configuration);
    assertNotNull(mongoClient);
  }

  @Test
  public void mongoDatabaseTest() {
    MongoClient mongoClient = mock(MongoClient.class);
    when(mongoClient.getDatabase(DATABASE)).thenReturn(null);

    mongoAutoConfigurer.mongoDatabase(configuration, mongoClient);

    verify(mongoClient).getDatabase(DATABASE);
  }

  @Test
  public void mongoDbFactoryTest() throws Exception {
    MongoClient mongo = mock(MongoClient.class);
    MongoConfiguration configuration = mock(MongoConfiguration.class);

    when(configuration.getDatabase()).thenReturn(DATABASE);

    SimpleMongoDbFactory factory = mongoAutoConfigurer.mongoDbFactory(mongo, configuration);

    verify(configuration).getDatabase();
    assertNotNull(factory);
  }

  @Test
  public void mongoInternalTest() {
    MongoDatabase mongoDatabase = mock(MongoDatabase.class);
    when(mongoDatabase.getCollection(dataMigrationProperties.getVersionTableName(), BasicDBObject.class)).thenReturn(null);

    MongoInternal mongoInternal = mongoAutoConfigurer.mongoInternal(mongoDatabase, dataMigrationProperties);

    assertNotNull(mongoInternal);
  }


  @Test
  public void mongoTemplateTest() throws Exception {
    MongoDbFactory mongoDbFactory = mock(MongoDbFactory.class);

    MongoTemplate mongoTemplate = mongoAutoConfigurer.mongoTemplate(mongoDbFactory);

    assertNotNull(mongoTemplate);
  }

  @Before
  public void setup() {
    initMocks(this);

    this.configuration = new MongoConfiguration();
    this.configuration.setHost(HOST);
    this.configuration.setPort(PORT);
    this.configuration.setUsername(USERNAME);
    this.configuration.setPassword(PASSWORD);
    this.configuration.setDatabase(DATABASE);
  }

}
