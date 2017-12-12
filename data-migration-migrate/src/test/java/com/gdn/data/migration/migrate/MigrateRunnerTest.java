package com.gdn.data.migration.migrate;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;

import com.gdn.data.migration.core.Migration;
import com.gdn.data.migration.core.MigrationInternal;

/**
 * Created by Vironica on 12/13/2016.
 */
public class MigrateRunnerTest {

  private static final Long VERSION = 0L;

  @InjectMocks
  private MigrateRunner migrateRunner;

  @Mock
  private MigrationInternal migrationInternal;

  @Mock
  private Migration migration;

  private List<Migration> migrations;
  private ApplicationContext applicationContext;

  @Test
  public void runTest() {
    try {
      this.migrateRunner.run();
      verify(this.migrationInternal).getMigrations(any(ApplicationContext.class));
      verify(this.migrationInternal).ensureVersion();
      verify(this.migrationInternal).canMigrate(MigrateRunnerTest.VERSION);
      verify(this.migrationInternal).updateVersion(MigrateRunnerTest.VERSION);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void runTest_withCannotMigrate() {
    when(this.migrationInternal.canMigrate(MigrateRunnerTest.VERSION)).thenReturn(Boolean.FALSE);
    try {
      this.migrateRunner.run();
      verify(this.migrationInternal).getMigrations(any(ApplicationContext.class));
      verify(this.migrationInternal).ensureVersion();
      verify(this.migrationInternal).canMigrate(MigrateRunnerTest.VERSION);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void runTest_withException() {
    doThrow(Throwable.class).when(this.migrationInternal).updateVersion(MigrateRunnerTest.VERSION);
    try {
      this.migrateRunner.run();
    } catch (Exception e) {
      verify(this.migrationInternal).getMigrations(any(ApplicationContext.class));
      verify(this.migrationInternal).ensureVersion();
      verify(this.migrationInternal).canMigrate(MigrateRunnerTest.VERSION);
      verify(this.migrationInternal).updateVersion(MigrateRunnerTest.VERSION);
      e.printStackTrace();
    }
  }

  @Test
  public void setApplicationContextTest() {
    this.migrateRunner.setApplicationContext(this.applicationContext);
  }

  @Before
  public void setUp() {
    initMocks(this);
    this.migrateRunner = new MigrateRunner(this.migrationInternal);

    this.migrations = new ArrayList<Migration>();
    this.migrations.add(this.migration);

    try {
      when(this.migrationInternal.getMigrations(any(ApplicationContext.class))).thenReturn(
          this.migrations);
      doNothing().when(this.migrationInternal).ensureVersion();
      when(this.migrationInternal.canMigrate(MigrateRunnerTest.VERSION)).thenReturn(Boolean.TRUE);
      doNothing().when(this.migrationInternal).updateVersion(MigrateRunnerTest.VERSION);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(this.migrationInternal);
  }
}
