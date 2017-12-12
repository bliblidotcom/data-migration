package com.gdn.data.migration.postgre;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;

/**
 * @author Rulando Irawan
 * @since 10/12/16
 */
public class PostgreServiceTest {

	@InjectMocks
	private PostgreService postgreService;
	
	@Before
	public void setUp() {
		initMocks(this);
	}
	
	@Test
	public void getContext_Test() {
		this.postgreService.getContext();
	}
}
