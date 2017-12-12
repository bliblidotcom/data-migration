package com.gdn.data.migration.migrate;

import static org.mockito.Mockito.times;
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

/**
 * @author Rulando Irawan
 * @since 13/12/2016
 */
public class MigrateAppTest {

	@InjectMocks
	private MigrateApp migrateApp;
	
	@Mock
	private ConfigurableApplicationContext applicationContext;
	
	@Mock
	private MigrateRunner migrateRunner;
	
	@Mock
	private AppFactory appFactory;
	
	@Test
	public void run_Test () throws Exception{
		String[] args = new String[] {"test1"};
		when(this.appFactory.springRun(MigrateApp.class, args)).thenReturn(applicationContext);
		when(this.applicationContext.getBean(MigrateRunner.class)).thenReturn(migrateRunner);
		
		this.migrateApp.run(args);
		
		verify(this.appFactory, times(1)).springRun(MigrateApp.class, args);
		verify(this.applicationContext, times(1)).getBean(MigrateRunner.class);
		verify(this.migrateRunner, times(1)).run();
		verify(this.applicationContext, times(1)).close();
	}
	
	@Test
	public void run_ExceptionTest () throws Exception{
		String[] args = new String[] {"test1"};
		when(this.appFactory.springRun(MigrateApp.class, args)).thenReturn(null);
		
		try {
			this.migrateApp.run(args);
		} catch (Exception e) {
			
		}
		
		verify(this.appFactory, times(1)).springRun(MigrateApp.class, args);
	}
	
	@Before
	public void setUp() {
		initMocks(this);
	}
	
	@After
	public void tearDown() {
		verifyNoMoreInteractions(this.appFactory);
		verifyNoMoreInteractions(this.applicationContext);
		verifyNoMoreInteractions(this.migrateRunner);
	}
}
