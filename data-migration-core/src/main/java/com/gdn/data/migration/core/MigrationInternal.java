package com.gdn.data.migration.core;

import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Eko Kurniawan Khannedy
 * @since 20/10/16
 */
public class MigrationInternal {

  private ApplicationContext applicationContext;

  public MigrationInternal(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public void ensureVersion() {
    applicationContext.getBeansOfType(Internal.class).values().forEach(Internal::ensureVersion);
  }

  public void updateVersion(Long version) {
    applicationContext.getBeansOfType(Internal.class).values().forEach(internal -> {
      internal.updateVersion(version);
    });
  }

  public Long currentVersion() {
    return applicationContext.getBeansOfType(Internal.class).values().stream()
        .map(Internal::currentVersion)
        .max(Long::compareTo).orElse(0L);
  }

  /**
   * Check is this version can migrate?
   *
   * @param version version
   * @return true if can migrate, false if not
   */
  public boolean canMigrate(Long version) {
    return currentVersion() < version;
  }

  /**
   * Get migration objects in ascending order
   *
   * @param applicationContext application context
   * @return sorted migration
   */
  public List<Migration> getMigrations(ApplicationContext applicationContext) {
    return applicationContext.getBeansOfType(Migration.class)
        .values().stream()
        .sorted((o1, o2) -> o1.version().compareTo(o2.version()))
        .collect(Collectors.toList());
  }

  /**
   * Get migration objects in descending order
   *
   * @param applicationContext application context
   * @return sorted migration
   */
  public List<Migration> getMigrationsReverse(ApplicationContext applicationContext) {
    return applicationContext.getBeansOfType(Migration.class)
        .values().stream()
        .sorted((o1, o2) -> o2.version().compareTo(o1.version()))
        .collect(Collectors.toList());
  }

}
