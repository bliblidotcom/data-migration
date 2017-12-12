package com.gdn.data.migration.migrate;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * @author Rulando Irawan
 * @since 12/13/2016
 */
public class AppFactoryTest {

	@InjectMocks
	private AppFactory appFactory;
	
	@Test
	public void springRun_Test() {
		String[] args = new String[] {"test"};		
		this.appFactory.springRun(MigrateApp.class, args);
	}
	
	@Before
	public void setUp() {
		initMocks(this);
	}
}
