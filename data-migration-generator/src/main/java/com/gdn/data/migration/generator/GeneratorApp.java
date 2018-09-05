package com.gdn.data.migration.generator;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @author Eko Kurniawan Khannedy
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {
    JooqAutoConfiguration.class,
    DataSourceAutoConfiguration.class,
    MongoAutoConfiguration.class,
    CassandraAutoConfiguration.class,
    MongoDataAutoConfiguration.class
}, excludeName = {
    "com.gdn.data.migration.elasticsearch.ElasticsearchAutoConfigurer",
    "com.gdn.data.migration.postgre.PostgreAutoConfigurer",
    "com.gdn.data.migration.mongo.MongoAutoConfigurer",
    "com.gdn.data.migration.cassandra.CassandraAutoConfigurer",
    "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration",
    "org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration",
    "org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration",
    "org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration",
    "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration"
})
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = {
    "com.gdn.data.migration.migrate.*.*",
    "com.gdn.data.migration.rollback.*.*",
    "migrations.*.*"
}))
public class GeneratorApp {

  private final AppFactory appFactory = new AppFactory();

  public void run(String[] args) throws Exception {
    ConfigurableApplicationContext applicationContext =
        this.appFactory.springAppRun(GeneratorApp.class, args);

    GeneratorRunner runner = applicationContext.getBean(GeneratorRunner.class);
    runner.run();

    applicationContext.close();
  }

}
