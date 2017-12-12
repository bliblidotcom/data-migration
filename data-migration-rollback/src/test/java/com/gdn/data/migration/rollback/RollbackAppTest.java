package com.gdn.data.migration.rollback;

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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

public class RollbackAppTest {


  @InjectMocks
  private RollbackApp rollbackApp;

  @Mock
  private AppFactory appFactory;

  @Mock
  private ConfigurableApplicationContext applicationContext;

  @Mock
  private RollbackRunner runner;

  private String[] args = new String[] {"0"};

  @Before
  public void setUp() throws Exception {
    initMocks(this);

    when(this.appFactory.springAppRun(RollbackApp.class, args)).thenReturn(applicationContext);
    when(this.applicationContext.getBean(RollbackRunner.class)).thenReturn(this.runner);
    doNothing().when(runner).run();

    ReflectionTestUtils.setField(this.rollbackApp, "appFactory", appFactory);
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(this.appFactory);
    verifyNoMoreInteractions(this.applicationContext);
    verifyNoMoreInteractions(this.runner);
  }

  @Test
  public void run() throws Exception {
    this.rollbackApp.run(args);
    verify(this.appFactory).springAppRun(RollbackApp.class, args);
    verify(this.applicationContext).getBean(RollbackRunner.class);
    verify(this.applicationContext).close();
    verify(this.runner).run();
  }

}
