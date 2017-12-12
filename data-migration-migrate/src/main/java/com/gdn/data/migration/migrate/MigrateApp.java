package com.gdn.data.migration.migrate;

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
@ComponentScan(basePackages = {"migrations", "beans", "com.gdn.data.migration.migrate"})
public class MigrateApp {

  private AppFactory migrateService = new AppFactory();

  public void run(String[] args) throws Exception {
    ConfigurableApplicationContext applicationContext = migrateService.springRun(MigrateApp.class, args);

    MigrateRunner runner = applicationContext.getBean(MigrateRunner.class);
    runner.run();

    applicationContext.close();
  }

}
