package com.gdn.data.migration.migrate;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Rulando Irawan
 * @since 12/13/2016
 */
public class AppFactory {

    public ConfigurableApplicationContext springRun (Class<MigrateApp> objectClass, String[] args) {
        return SpringApplication.run(objectClass, args);
    }
}
