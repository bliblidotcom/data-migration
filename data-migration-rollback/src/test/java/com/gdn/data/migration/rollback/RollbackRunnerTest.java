package com.gdn.data.migration.rollback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationContext;

import com.gdn.data.migration.core.Migration;
import com.gdn.data.migration.core.MigrationInternal;

/**
 * @author Henry Jonathan
 * @since 09/12/16
 */
public class RollbackRunnerTest {

  @InjectMocks
  private RollbackRunner rollbackRunner;

  @Mock
  private ApplicationContext applicationContext;

  @Mock
  private MigrationInternal migrationInternal;

  private static final Long VERSION_1 = 1L;
  private static final Long VERSION_2 = 2L;
  private static final Long VERSION_3 = 3L;
  private static final Long VERSION_4 = 4L;
  private static final String VERSION = "version";

  private List<Migration> migrations;
  private List<Migration> migrations2;
  private Long currentInternalVersion;
  private int rollbackCounter;

  @Before
  public void setUp() {
    initMocks(this);

    this.rollbackCounter = 0;
    this.rollbackRunner.setApplicationContext(this.applicationContext);

    Migration migration1 = contructMigration1();
    Migration migration2 = contructMigration2();
    Migration migration3 = contructMigration3();

    this.migrations = new ArrayList<Migration>(3);
    this.migrations.add(migration3);
    this.migrations.add(migration2);
    this.migrations.add(migration1);

    this.migrations2 = new ArrayList<Migration>(1);
    this.migrations2.add(migration2);

    when(this.migrationInternal.getMigrationsReverse(this.applicationContext))
        .thenReturn(this.migrations);
    when(this.migrationInternal.canMigrate(any(Long.class))).then(new Answer<Boolean>() {
      @Override
      public Boolean answer(InvocationOnMock invocation) throws Throwable {
        Object[] objs = invocation.getArguments();
        Long version = (Long) objs[0];
        return currentInternalVersion < version;
      }
    });

    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        Object[] objs = invocation.getArguments();
        currentInternalVersion = (Long) objs[0];
        return null;
      }
    }).when(this.migrationInternal).updateVersion(any(Long.class));
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(this.applicationContext);
    verifyNoMoreInteractions(this.migrationInternal);
  }

  @Test
  public void testRollbackToLowerVersion() throws Exception {
    System.setProperty(VERSION, VERSION_1.toString());
    this.currentInternalVersion = VERSION_2;

    when(this.migrationInternal.currentVersion()).thenReturn(this.currentInternalVersion);

    this.rollbackRunner.run();

    verify(this.migrationInternal).ensureVersion();
    verify(this.migrationInternal).currentVersion();
    verify(this.migrationInternal).getMigrationsReverse(applicationContext);
    verify(this.migrationInternal, times(this.migrations.size())).canMigrate(any(Long.class));
    verify(this.migrationInternal).updateVersion(VERSION_1);

    assertEquals(rollbackCounter, 1);
    assertEquals(Long.valueOf(this.currentInternalVersion.compareTo(VERSION_1)), Long.valueOf(0));
  }

  @Test
  public void testRollbackToLowerVersion2() throws Exception {
    System.setProperty(VERSION, VERSION_1.toString());
    this.currentInternalVersion = VERSION_2;
    when(this.migrationInternal.currentVersion()).thenReturn(this.currentInternalVersion);

    when(this.migrationInternal.getMigrationsReverse(this.applicationContext))
        .thenReturn(this.migrations2);

    this.rollbackRunner.run();

    verify(this.migrationInternal).ensureVersion();
    verify(this.migrationInternal).currentVersion();
    verify(this.migrationInternal).getMigrationsReverse(applicationContext);
    verify(this.migrationInternal, times(this.migrations2.size())).canMigrate(any(Long.class));
    verify(this.migrationInternal).updateVersion(0L);

    assertEquals(rollbackCounter, 1);
    assertEquals(Long.valueOf(this.currentInternalVersion.compareTo(VERSION_1)), Long.valueOf(-1));
  }

  @Test
  public void testRollbackWithException() throws Exception {
    System.setProperty(VERSION, VERSION_1.toString());
    this.currentInternalVersion = VERSION_3;
    when(this.migrationInternal.currentVersion()).thenReturn(this.currentInternalVersion);

    Migration exceptionMigration = new Migration() {

      @Override
      public void migrate() throws Exception {}

      @Override
      public String name() {
        return null;
      }

      @Override
      public void rollback() throws Exception {
        throw new Exception("Expected exception");
      }

      @Override
      public Long version() {
        return VERSION_2;
      }
    };

    this.migrations.remove(1);
    this.migrations.add(1, exceptionMigration);

    try {
      this.rollbackRunner.run();
    } catch (Exception ex) {
      assertTrue(ex instanceof RuntimeException);
    }

    verify(this.migrationInternal).ensureVersion();
    verify(this.migrationInternal).currentVersion();
    verify(this.migrationInternal).getMigrationsReverse(this.applicationContext);
    verify(this.migrationInternal, times(2)).canMigrate(any(Long.class));
    verify(this.migrationInternal).updateVersion(VERSION_2);

    assertEquals(this.rollbackCounter, 1);
    assertEquals(Long.valueOf(this.currentInternalVersion.compareTo(VERSION_2)), Long.valueOf(0));
  }

  @Test(expected = RuntimeException.class)
  public void testRollbackWithExceptionToHigherVersion() throws Exception {
    System.setProperty(VERSION, VERSION_4.toString());
    this.currentInternalVersion = VERSION_3;
    when(this.migrationInternal.currentVersion()).thenReturn(this.currentInternalVersion);

    try {
      this.rollbackRunner.run();
    } catch (RuntimeException ex) {
      verify(this.migrationInternal).ensureVersion();
      verify(this.migrationInternal).currentVersion();
      assertEquals(this.rollbackCounter, 0);
      assertEquals(Long.valueOf(this.currentInternalVersion.compareTo(VERSION_3)), Long.valueOf(0));

      throw ex;
    }
  }

  @Test(expected = RuntimeException.class)
  public void testRollbackWithExceptionToSameVersion() throws Exception {
    System.setProperty(VERSION, VERSION_2.toString());
    this.currentInternalVersion = VERSION_2;
    when(this.migrationInternal.currentVersion()).thenReturn(this.currentInternalVersion);

    try {
      this.rollbackRunner.run();
    } catch (RuntimeException ex) {
      verify(this.migrationInternal).ensureVersion();
      verify(this.migrationInternal).currentVersion();
      assertEquals(this.rollbackCounter, 0);
      assertEquals(Long.valueOf(this.currentInternalVersion.compareTo(VERSION_2)), Long.valueOf(0));

      throw ex;
    }
  }

  private Migration contructMigration1() {
    return new Migration() {

      @Override
      public void migrate() throws Exception {}

      @Override
      public String name() {
        return null;
      }

      @Override
      public void rollback() throws Exception {
        rollbackCounter++;
      }

      @Override
      public Long version() {
        return VERSION_1;
      }
    };
  }

  private Migration contructMigration2() {
    return new Migration() {

      @Override
      public void migrate() throws Exception {}

      @Override
      public String name() {
        return null;
      }

      @Override
      public void rollback() throws Exception {
        rollbackCounter++;
      }

      @Override
      public Long version() {
        return VERSION_2;
      }
    };
  }

  private Migration contructMigration3() {
    return new Migration() {

      @Override
      public void migrate() throws Exception {}

      @Override
      public String name() {
        return null;
      }

      @Override
      public void rollback() throws Exception {
        rollbackCounter++;
      }

      @Override
      public Long version() {
        return VERSION_3;
      }
    };
  }
}
