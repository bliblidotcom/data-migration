package com.gdn.data.migration.generator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by lukman.h on 12/10/2016.
 */
public class GeneratorConfigurationTest {

    private static final String TARGET = "src/main/java/migrations/";
    private static final String PREFIX = "Migration_";
    private static final String SUFFIX = ".java";

    private GeneratorConfiguration generatorConfiguration;

    @Test
    public void getTargetTest() {
        String result = generatorConfiguration.getTarget();
        assertThat(result, equalTo(TARGET));
    }

    @Test
    public void getPrefixTest() {
        String result = generatorConfiguration.getPrefix();
        assertThat(result, equalTo(PREFIX));
    }

    @Test
    public void getSuffixTest() {
        String result = generatorConfiguration.getSuffix();
        assertThat(result, equalTo(SUFFIX));
    }

    @Before
    public void setUp() {
        generatorConfiguration = new GeneratorConfiguration();
    }
}
