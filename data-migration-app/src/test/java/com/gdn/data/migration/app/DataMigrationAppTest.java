package com.gdn.data.migration.app;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.gdn.data.migration.generator.GeneratorApp;
import com.gdn.data.migration.migrate.MigrateApp;
import com.gdn.data.migration.rollback.RollbackApp;

/**
 * Created by indra.e.prasetya on 12/9/2016.
 */
public class DataMigrationAppTest {

  @InjectMocks
  private DataMigrationApp dataMigrationApp;

  @Mock
  private AppFactory appFactory;

  @Mock
  private GeneratorApp generatorApp;

  @Mock
  private MigrateApp migrateApp;

  @Mock
  private RollbackApp rollbackApp;

  private String[] args = new String[]{};

  @Before
  public void setUp() throws Exception {
    initMocks(this);

    when(appFactory.getGeneratorApp()).thenReturn(generatorApp);
    when(appFactory.getMigrateApp()).thenReturn(migrateApp);
    when(appFactory.getRollbackApp()).thenReturn(rollbackApp);

    doNothing().when(generatorApp).run(args);
    doNothing().when(migrateApp).run(args);
    doNothing().when(rollbackApp).run(args);

    ReflectionTestUtils.setField(DataMigrationApp.class, "appFactory", appFactory);
  }

  @Test
  public void runCreate() throws Exception {
    ReflectionTestUtils.setField(DataMigrationApp.class, "mode", "create");

    dataMigrationApp.run(args);

    verify(appFactory).getGeneratorApp();
    verify(generatorApp).run(args);
  }

  @Test
  public void runMigrate() throws Exception {
    ReflectionTestUtils.setField(DataMigrationApp.class, "mode", "migrate");

    dataMigrationApp.run(args);

    verify(appFactory).getMigrateApp();
    verify(migrateApp).run(args);
  }

  @Test
  public void runRollback() throws Exception {
    ReflectionTestUtils.setField(DataMigrationApp.class, "mode", "rollback");

    dataMigrationApp.run(args);

    verify(appFactory).getRollbackApp();
    verify(rollbackApp).run(args);
  }

  @Test(expected = IllegalArgumentException.class)
  public void runWithoutMode() throws Exception {
    ReflectionTestUtils.setField(DataMigrationApp.class, "mode", null);

    dataMigrationApp.run(args);
  }

  @Test(expected = IllegalArgumentException.class)
  public void runUnknownMode() throws Exception {
    ReflectionTestUtils.setField(DataMigrationApp.class, "mode", "random");

    dataMigrationApp.run(args);
  }

  @Test(expected = IllegalArgumentException.class)
  public void runModeFail() throws Exception {
    ReflectionTestUtils.setField(DataMigrationApp.class, "mode", "fail");
    String mode = "fail";

    dataMigrationApp.runMode(mode, args);
  }

  @Test(expected = IllegalArgumentException.class)
  public void runModeFailEmpty() throws Exception {
    ReflectionTestUtils.setField(DataMigrationApp.class, "mode", "");
    String mode = "";

    dataMigrationApp.runMode(mode, args);
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(this.appFactory);
    verifyNoMoreInteractions(this.migrateApp);
    verifyNoMoreInteractions(this.generatorApp);
    verifyNoMoreInteractions(this.rollbackApp);
  }
}
