package com.gdn.data.migration.app;

import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.gdn.data.migration.generator.GeneratorApp;
import com.gdn.data.migration.migrate.MigrateApp;
import com.gdn.data.migration.rollback.RollbackApp;

/**
 * Created by mashuri.hasan on 14-DEC-2016.
 */
public class AppFactoryTest {

  @InjectMocks
  private AppFactory appFactory;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
  }

  @Test
  public void getGeneratorAppTest() {
    GeneratorApp result = this.appFactory.getGeneratorApp();
    assertNotNull(result);
  }

  @Test
  public void getMigrateAppTEst() {
    MigrateApp result = this.appFactory.getMigrateApp();
    assertNotNull(result);
  }

  @Test
  public void getRollbackAppTest() {
    RollbackApp result = this.appFactory.getRollbackApp();
    assertNotNull(result);
  }

}
