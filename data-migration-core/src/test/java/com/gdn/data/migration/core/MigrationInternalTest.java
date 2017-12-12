package com.gdn.data.migration.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;

/**
 * Created by mashuri.hasan on 8 December 2016.
 */
public class MigrationInternalTest {

  private static final Long version0 = 0L;
  private static final Long version1 = 1L;

  @InjectMocks
  private MigrationInternal migrationInternal;

  @Mock
  private ApplicationContext applicationContext;

  @Before
  public void setUp() {
    initMocks(this);

    Map<String, Internal> internal = new HashMap<>();
    internal.put("1", mock(Internal.class));
    internal.put("2", mock(Internal.class));
    when(applicationContext.getBeansOfType(Internal.class)).thenReturn(internal);

    Map<String, Migration> migration = new HashMap<>();
    migration.put("1", mock(Migration.class));
    migration.put("2", mock(Migration.class));
    when(applicationContext.getBeansOfType(Migration.class)).thenReturn(migration);
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(this.applicationContext);
  }

  @Test
  public void testEnsureVersion() {
    this.migrationInternal.ensureVersion();
    verify(this.applicationContext).getBeansOfType(Internal.class);
  }

  @Test
  public void testUpdateVersion() {
    this.migrationInternal.updateVersion(version1);
    verify(this.applicationContext).getBeansOfType(Internal.class);
  }

  @Test
  public void testCurrentVersion() {
    Long result = this.migrationInternal.currentVersion();
    assertNotNull(result);
    assertEquals(Long.valueOf(0), result);
    verify(this.applicationContext).getBeansOfType(Internal.class);
  }

  @Test
  public void testCanMigrate() {
    boolean result = this.migrationInternal.canMigrate(version1);
    assertTrue(result);
    verify(this.applicationContext).getBeansOfType(Internal.class);
  }

  @Test
  public void testCanMigrateM() {
    boolean result = this.migrationInternal.canMigrate(version0);
    assertFalse(result);
    verify(this.applicationContext).getBeansOfType(Internal.class);
  }

  @Test
  public void testGetMigrations() {
    List<Migration> results = this.migrationInternal.getMigrations(applicationContext);
    assertNotNull(results);
    verify(this.applicationContext).getBeansOfType(Migration.class);
  }

  @Test
  public void testGetMigrationsReverse() {
    List<Migration> results = this.migrationInternal.getMigrationsReverse(applicationContext);
    assertNotNull(results);
    verify(this.applicationContext).getBeansOfType(Migration.class);
  }

}
