package com.gdn.data.migration.rollback;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Eko Kurniawan Khannedy
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {
    JooqAutoConfiguration.class,
    DataSourceAutoConfiguration.class,
    MongoAutoConfiguration.class,
    CassandraAutoConfiguration.class
})
@ComponentScan(basePackages = {"migrations", "beans", "com.gdn.data.migration.rollback"})
public class RollbackApp {
  private final AppFactory appFactory = new AppFactory();

  public void run(String[] args) throws Exception {
    ConfigurableApplicationContext applicationContext =
        this.appFactory.springAppRun(RollbackApp.class, args);

    RollbackRunner runner = applicationContext.getBean(RollbackRunner.class);
    runner.run();

    applicationContext.close();
  }

}
