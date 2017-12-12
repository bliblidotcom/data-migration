package com.gdn.data.migration.app;

import com.gdn.data.migration.generator.GeneratorApp;
import com.gdn.data.migration.migrate.MigrateApp;
import com.gdn.data.migration.rollback.RollbackApp;

/**
 * Created by indra.e.prasetya on 12/8/2016.
 */
public class AppFactory {

  public GeneratorApp getGeneratorApp() {
    return new GeneratorApp();
  }

  public MigrateApp getMigrateApp() {
    return new MigrateApp();
  }

  public RollbackApp getRollbackApp() {
    return new RollbackApp();
  }

}
