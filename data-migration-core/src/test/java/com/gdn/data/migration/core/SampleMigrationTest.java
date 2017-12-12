package com.gdn.data.migration.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by louis.ompusunggu on 14/12/2016.
 */
public class SampleMigrationTest {

    private SampleMigration sampleMigration;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        sampleMigration = new SampleMigration();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void versionTest() throws Exception {
        sampleMigration.version();
    }

    @Test
    public void nameTest() throws Exception {
        sampleMigration.name();
    }

    @Test
    public void migrateTest() throws Exception {
        sampleMigration.migrate();
    }

    @Test
    public void rollbackTest() throws Exception {
        sampleMigration.rollback();
    }
}