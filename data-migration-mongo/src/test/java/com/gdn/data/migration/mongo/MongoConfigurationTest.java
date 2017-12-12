package com.gdn.data.migration.mongo;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by louis.ompusunggu on 10/12/2016.
 */
public class MongoConfigurationTest {

    private MongoConfiguration mongoConfiguration = new MongoConfiguration();

    @Before
    public void setUp() {
        initMocks(this);
        mongoConfiguration.setUsername("username");
        mongoConfiguration.setHost("host");
        mongoConfiguration.setPort(8080);
        mongoConfiguration.setDatabase("database");
        mongoConfiguration.setPassword("password");
    }

    @Test
    public void getUsernameTest() throws Exception {
        String username = mongoConfiguration.getUsername();
        assertTrue(username == "username");
    }

    @Test
    public void setUsernameTest() throws Exception {
        mongoConfiguration.setUsername("username");
        assertTrue(mongoConfiguration.getUsername() == "username");
    }

    @Test
    public void getPasswordTest() throws Exception {
        String password = mongoConfiguration.getPassword();
        assertTrue(password == "password");
    }

    @Test
    public void setPasswordTest() throws Exception {
        mongoConfiguration.setPassword("password");
        assertTrue(mongoConfiguration.getPassword() == "password");
    }

    @Test
    public void getHostTest() throws Exception {
        String host = mongoConfiguration.getHost();
        assertTrue(host == "host");
    }

    @Test
    public void setHostTest() throws Exception {
        mongoConfiguration.setHost("host");
        assertTrue(mongoConfiguration.getHost() == "host");
    }

    @Test
    public void getPortTest() throws Exception {
        int port = mongoConfiguration.getPort();
        assertTrue(port == 8080);
    }

    @Test
    public void setPortTest() throws Exception {
        mongoConfiguration.setPort(8080);
        assertTrue(mongoConfiguration.getPort() == 8080);
    }

    @Test
    public void getDatabaseTest() throws Exception {
        String db = mongoConfiguration.getDatabase();
        assertTrue(db == "database");
    }

    @Test
    public void setDatabaseTest() throws Exception {
        mongoConfiguration.setDatabase("database");
        assertTrue(mongoConfiguration.getDatabase() == "database");
    }
}