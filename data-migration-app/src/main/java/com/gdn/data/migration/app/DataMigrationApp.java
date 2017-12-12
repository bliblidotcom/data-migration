package com.gdn.data.migration.app;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Eko Kurniawan Khannedy
 */
public class DataMigrationApp {

  private static AppFactory appFactory = new AppFactory();
  private static String mode = System.getProperty("mode");

  private static final String CREATE = "create";
  private static final String MIGRATE = "migrate";
  private static final String ROLLBACK = "rollback";

  /**
   * Run data migration app
   *
   * @param args command args
   * @throws Exception exception
   */
  public static void run(String[] args) throws Exception {
    String mode = getMode();
    DataMigrationApp.runMode(mode, args);
  }

  /**
   * Run application
   *
   * @param mode mode
   * @param args arguments
   * @throws Exception exception
   */
  public static void runMode(String mode, String[] args) throws Exception {
    if (StringUtils.isEmpty(mode)) {
      throw new IllegalArgumentException("Unknown application context for mode " + mode);
    }
    if (mode.equals(CREATE)) {
      appFactory.getGeneratorApp().run(args);
    } else if (mode.equals(MIGRATE)) {
      appFactory.getMigrateApp().run(args);
    } else if (mode.equals(ROLLBACK)) {
      appFactory.getRollbackApp().run(args);
    } else {
      throw new IllegalArgumentException("Unknown application context for mode " + mode);
    }
  }

  /**
   * Get mode from system parameter mode
   *
   * @return mode (create, migrate or rollback)
   * @throws IllegalArgumentException if mode is invalid
   */
  public static String getMode() {
    if (mode != null && (mode.equals(CREATE) || mode.equals(MIGRATE) || mode.equals(ROLLBACK))) {
      return mode;
    } else {
      throw new IllegalArgumentException("Unknown mode " + mode);
    }
  }

}
