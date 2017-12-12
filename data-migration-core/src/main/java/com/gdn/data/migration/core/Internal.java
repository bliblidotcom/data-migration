package com.gdn.data.migration.core;

/**
 * Internal implementation of migration
 *
 * @author Eko Kurniawan Khannedy
 * @since 20/10/16
 */
public interface Internal {

  String VERSION_NAME = "blibli_migration_version";

  /**
   * Ensure version is exists in datastore
   */
  void ensureVersion();

  /**
   * Update version in datastore
   *
   * @param version version
   */
  void updateVersion(Long version);

  /**
   * Get current version in datastore
   *
   * @return current version
   */
  Long currentVersion();

}
