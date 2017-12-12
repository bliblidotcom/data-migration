package com.gdn.data.migration.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by louis.ompusunggu on 08/12/2016.
 */
public class DataMigrationAutoConfigurerTest {

    @InjectMocks
    private DataMigrationAutoConfigurer dataMigrationAutoConfigurer;

    @Autowired
    private ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void migrationInternalTest() throws Exception {
        MigrationInternal migrationInternal = this.dataMigrationAutoConfigurer.migrationInternal();
        assert(migrationInternal != null);
    }

    @Test
    public void objectMapperTest() throws Exception {
        ObjectMapper mapper = this.dataMigrationAutoConfigurer.objectMapper();

        assert (mapper != null);
    }

    @Test
    public void setApplicationContextTest() throws Exception {
        this.dataMigrationAutoConfigurer.setApplicationContext(this.applicationContext);
    }
}