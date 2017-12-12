package com.gdn.data.migration.generator;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Created by lukman.h on 12/10/2016.
 */
public class GeneratorAppTest {

  @InjectMocks
  private GeneratorApp generatorApp;

  @Mock
  private SpringApplication springApplication;

  @Mock
  private AppFactory appFactory;

  @Mock
  private GeneratorRunner runner;

  @Mock
  private ConfigurableApplicationContext configurableApplicationContext;

  private final String[] args = new String[] {"0"};

  @Test
  public void run() throws Exception {
    this.generatorApp.run(args);
    verify(appFactory).springAppRun(GeneratorApp.class, args);
  }

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(this.appFactory.springAppRun(GeneratorApp.class, args)).thenReturn(
        configurableApplicationContext);

    when(this.configurableApplicationContext.getBean(GeneratorRunner.class))
        .thenReturn(this.runner);

    doNothing().when(runner).run();

    ReflectionTestUtils.setField(this.generatorApp, "appFactory", appFactory, AppFactory.class);
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(this.springApplication);
    verifyNoMoreInteractions(this.appFactory);
  }
}
