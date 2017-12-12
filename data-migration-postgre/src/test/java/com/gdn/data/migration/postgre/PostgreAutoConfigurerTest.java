package com.gdn.data.migration.postgre;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.postgresql.ds.PGPoolingDataSource;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;

/**
 * Created by indra.e.prasetya on 12/13/2016.
 */
public class PostgreAutoConfigurerTest {

  @InjectMocks
  private PostgreAutoConfigurer postgreAutoConfigurer;

  private static final String USERNAME = "USERNAME";
  private static final String PASSWORD = "PASSWORD";
  private static final String SERVER_NAME = "SERVER NAME";
  private static final int PORT_NUMBER = 123;
  private static final String DATABASE_NAME = "DATABASE NAME";

  private PostgreConfiguration postgreConfiguration;

  @Before
  public void setUp() {
    initMocks(this);

    this.postgreConfiguration = new PostgreConfiguration();
    this.postgreConfiguration.setUsername(USERNAME);
    this.postgreConfiguration.setPassword(PASSWORD);
    this.postgreConfiguration.setHost(SERVER_NAME);
    this.postgreConfiguration.setPort(PORT_NUMBER);
    this.postgreConfiguration.setDatabase(DATABASE_NAME);
  }

  @Test
  public void pgPoolingDataSource() {
    PGPoolingDataSource ds = postgreAutoConfigurer.pgPoolingDataSource(this.postgreConfiguration);

    assertEquals(USERNAME, ds.getUser());
    assertEquals(PASSWORD, ds.getPassword());
    assertEquals(SERVER_NAME, ds.getServerName());
    assertEquals(PORT_NUMBER, ds.getPortNumber());
    assertEquals(DATABASE_NAME, ds.getDatabaseName());
  }

  @Test
  public void postgreService() {
    PGPoolingDataSource ds = postgreAutoConfigurer.pgPoolingDataSource(this.postgreConfiguration);

    PostgreService ps = postgreAutoConfigurer.postgreService(ds);
  }

  @Test
  public void postgreInternal() {
    PGPoolingDataSource ds = postgreAutoConfigurer.pgPoolingDataSource(this.postgreConfiguration);

    PostgreService ps = postgreAutoConfigurer.postgreService(ds);

    PostgreInternal pi = postgreAutoConfigurer.postgreInternal(ps);
  }
}
