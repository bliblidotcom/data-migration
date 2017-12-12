package com.gdn.data.migration.generator;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by lukman.h on 12/10/2016.
 */
public class AppFactoryTest {

  @InjectMocks
  private AppFactory appFactory;

  private final String[] args = new String[] {"0"};

  @Test
  public void run() throws Exception {
    this.appFactory.springAppRun(Object.class, args);
  }

  @Before
  public void setUp() throws Exception {
    initMocks(this);
  }

  @After
  public void tearDown() {}
}
