package com.gdn.data.migration.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by lukman.h on 12/10/2016.
 */
public class AppFactory {
    public ConfigurableApplicationContext springAppRun(Object source, String... args) {
        return SpringApplication.run(source, args);
    }
}
