package com.gdn.data.migration.rollback;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.Assert.*;

/**
 * Created by a on 12/10/16.
 */
public class AppFactoryTest {

    @InjectMocks
    private AppFactory appFactory;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {}

    @Test
    public void springAppRun() throws Exception {
        ConfigurableApplicationContext configurableApplicationContext = this.appFactory.springAppRun(Object.class, new String[]{});
        assertNotNull(configurableApplicationContext);
    }

}