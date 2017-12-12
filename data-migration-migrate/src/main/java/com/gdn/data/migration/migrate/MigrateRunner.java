package com.gdn.data.migration.migrate;

import com.gdn.data.migration.core.Migration;
import com.gdn.data.migration.core.MigrationInternal;
import com.gdn.data.migration.core.Runner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Runner for migrate migration script
 *
 * @author Eko Kurniawan Khannedy
 * @since 19/10/16
 */
@Component
public class MigrateRunner implements Runner, ApplicationContextAware {

  private static final Logger LOG = LoggerFactory.getLogger(MigrateRunner.class);

  private ApplicationContext applicationContext;

  private MigrationInternal migrationInternal;

  @Autowired
  public MigrateRunner(MigrationInternal migrationInternal) {
    this.migrationInternal = migrationInternal;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void run() throws Exception {
    LOG.info("Start migration");
    List<Migration> migrations = migrationInternal.getMigrations(applicationContext);

    LOG.info("Ensure migration version");
    migrationInternal.ensureVersion();
    migrations.forEach(migration -> {
      if (migrationInternal.canMigrate(migration.version())) {
        LOG.info("Start migration {} : {}", migration.version(), migration.name());
        try {
          migration.migrate();
          migrationInternal.updateVersion(migration.version());
          LOG.info("Success migration {} : {}", migration.version(), migration.name());
        } catch (Throwable ex) {
          LOG.error("Error migration {} : {}", migration.version(), migration.name());
          throw new RuntimeException(ex);
        }
      }
    });
    LOG.info("Finish all migration processes");
  }
}
