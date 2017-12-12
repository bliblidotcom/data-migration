package com.gdn.data.migration.rollback;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class AppFactory {
  public ConfigurableApplicationContext springAppRun(Object source, String... args) {
    return SpringApplication.run(RollbackApp.class, args);
  }
}
