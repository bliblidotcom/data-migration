package com.gdn.data.migration.core;

/**
 * @author Eko Kurniawan Khannedy
 * @since 19/10/16
 */
public interface Migration {

  /**
   * Migration version
   *
   * @return version
   */
  Long version();

  /**
   * Migration name
   *
   * @return name
   */
  String name();

  /**
   * This method will called on migrate
   */
  void migrate() throws Exception;

  /**
   * This method will called on rollback
   */
  void rollback() throws Exception;

}
