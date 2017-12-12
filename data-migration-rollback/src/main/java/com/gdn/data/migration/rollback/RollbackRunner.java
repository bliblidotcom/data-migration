package com.gdn.data.migration.rollback;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.gdn.data.migration.core.Migration;
import com.gdn.data.migration.core.MigrationInternal;
import com.gdn.data.migration.core.Runner;

/**
 * Runner for rollback migration script
 *
 * @author Eko Kurniawan Khannedy
 * @since 19/10/16
 */
@Component
public class RollbackRunner implements Runner, ApplicationContextAware {

  private static final Logger LOG = LoggerFactory.getLogger(RollbackRunner.class);

  private ApplicationContext applicationContext;

  private MigrationInternal migrationInternal;

  @Autowired
  public RollbackRunner(MigrationInternal migrationInternal) {
    this.migrationInternal = migrationInternal;
  }

  private Long getPreviousMigrationVersion(List<Migration> migrationsReversed, Migration migration) {
    int index = migrationsReversed.lastIndexOf(migration);
    boolean isNotFirstVersion = index < migrationsReversed.size() - 1;
    return isNotFirstVersion ? migrationsReversed.get(index + 1).version() : 0L;
  }

  @Override
  public void run() throws Exception {
    Long toVersion = Long.valueOf(System.getProperty("version"));
    Long currentVersion = migrationInternal.currentVersion();

    LOG.info("Ensure migration version");
    migrationInternal.ensureVersion();

    if (currentVersion > toVersion) {
      LOG.info("Start rollback to version {}", toVersion);
      List<Migration> migrations = migrationInternal.getMigrationsReverse(applicationContext);

      migrations.forEach(migration -> {
        if (migrationInternal.canMigrate(migration.version())) {
          // unmigrate migration script, don't rollback it!
        } else {
          if (migration.version() > toVersion) {
            LOG.info("Start rollback {} : {}", migration.version(), migration.name());
            try {
              migration.rollback();
              migrationInternal.updateVersion(getPreviousMigrationVersion(migrations, migration));
              LOG.info("Success rollback {} : {}", migration.version(), migration.name());
            } catch (Throwable ex) {
              LOG.error("Error rollback {} : {}", migration.version(), migration.name());
              throw new RuntimeException(ex);
            }
          }
        }
      });

      LOG.info("Finish all rollback processes");
    } else {
      throw new RuntimeException("Current Version: " + currentVersion + ", Target Version: "
          + toVersion + ". Cannot rollback to same or higher version.");
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

}
