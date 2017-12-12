package com.gdn.data.migration.postgre;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;

import org.jooq.Constraint;
import org.jooq.CreateTableAsStep;
import org.jooq.CreateTableColumnStep;
import org.jooq.CreateTableConstraintStep;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSelectStep;
import org.jooq.UpdateConditionStep;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.SQLDataType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.any;

/**
 * @author Rulando Irawan
 * @since 10/12/16
 */
public class PostgreInternalTest {

	private static final Integer INTEGER_VALUE = 9;
	private static final Long LONG_VALUE = 16l;

	private static final String ID = "id";
	private static final String VALUE = "value";
	private static final String VERSION_NAME = "blibli_migration_version";
	
	private static final int INT_VALUE = 16;
	public static final String FIELD_ID = "id";

	private PostgreInternal postgreInternal;
	
	@Mock
	private CreateTableAsStep<Record> createTableAsStep;
	
	@Mock
	private CreateTableColumnStep createTableColumnStep;
	
	@Mock
	private CreateTableConstraintStep createTableConstraintStep;
	
	@Mock
	private DSLContext dslContext;

	@Mock
	private InsertSetMoreStep<Record> insertSetMoreStep;
	
	@Mock
	private InsertSetStep<Record> insertSetStep;

	@Mock
	private PostgreService postgreService;
	
	@Mock
	private Record1<Long> recordValue;
	
	@Mock
	private SelectConditionStep<Record1<Long>> selectConditionStep;
	
	@Mock
	private SelectJoinStep<Record1<Long>> selectJoinStep;
	
	@Mock
	private SelectSelectStep<Record1<Long>> selectSelectStep;
	
	@Mock
	private UpdateConditionStep<Record> updateConditionStep;
	
	@Mock
	private UpdateSetFirstStep<Record> updateSetFirstStep;
	
	@Mock
	private UpdateSetMoreStep<Record> updateSetMoreStep;

	
	@Test
	public void currentVersion_Test() {
		when(this.postgreService.getContext()).thenReturn(dslContext);
		when(this.dslContext.select(field(PostgreInternalTest.VALUE, SQLDataType.BIGINT))).thenReturn(selectSelectStep);
		when(this.selectSelectStep.from(any(String.class))).thenReturn(selectJoinStep);
		when(this.selectJoinStep.where(field(FIELD_ID).eq(VERSION_NAME))).thenReturn(selectConditionStep);
		when(this.selectConditionStep.fetchOne()).thenReturn(recordValue);
		when(this.recordValue.value1()).thenReturn(PostgreInternalTest.LONG_VALUE);

		Long result = this.postgreInternal.currentVersion();
		assertEquals(result, LONG_VALUE);
	}

	@Test
	public void ensureVersion_Test() {
		when(this.postgreService.getContext()).thenReturn(dslContext);
		when(this.dslContext.createTableIfNotExists(any(String.class))).thenReturn(createTableAsStep);
		when(this.createTableAsStep.column(PostgreInternalTest.ID, SQLDataType.VARCHAR)).thenReturn(createTableColumnStep);
		when(this.createTableColumnStep.column(PostgreInternalTest.VALUE, SQLDataType.BIGINT)).thenReturn(createTableColumnStep);
		when(this.createTableColumnStep.constraints(any(Constraint.class))).thenReturn(createTableConstraintStep);
		when(this.createTableConstraintStep.execute()).thenReturn(INTEGER_VALUE);
		when(this.dslContext.fetchCount(table(name(VERSION_NAME)))).thenReturn(0);
		when(this.dslContext.insertInto(table(name(VERSION_NAME)))).thenReturn(insertSetStep);
		when(this.insertSetStep.set(field(PostgreInternalTest.ID), VERSION_NAME)).thenReturn(insertSetMoreStep);
		when(this.insertSetMoreStep.set(field(PostgreInternalTest.VALUE), 0L)).thenReturn(insertSetMoreStep);
		when(this.insertSetMoreStep.execute()).thenReturn(INTEGER_VALUE);
		
		this.postgreInternal.ensureVersion();
	}
	
	@Test
	public void ensureVersion_TestFalse() {
		when(this.postgreService.getContext()).thenReturn(dslContext);
		when(this.dslContext.createTableIfNotExists(any(String.class))).thenReturn(createTableAsStep);
		when(this.createTableAsStep.column(PostgreInternalTest.ID, SQLDataType.VARCHAR)).thenReturn(createTableColumnStep);
		when(this.createTableColumnStep.column(PostgreInternalTest.VALUE, SQLDataType.BIGINT)).thenReturn(createTableColumnStep);
		when(this.createTableColumnStep.constraints(any(Constraint.class))).thenReturn(createTableConstraintStep);
		when(this.createTableConstraintStep.execute()).thenReturn(INTEGER_VALUE);
		when(this.dslContext.fetchCount(table(name(VERSION_NAME)))).thenReturn(INT_VALUE);

		this.postgreInternal.ensureVersion();
	}
	
	@Before
	public void setUp() {
		initMocks(this);
		postgreInternal = new PostgreInternal(postgreService);
	}
	
	@Test
	public void updateVersion_Test() {
		when(this.postgreService.getContext()).thenReturn(dslContext);
		when(this.dslContext.update(any())).thenReturn(updateSetFirstStep);
		when(this.updateSetFirstStep.set(field(PostgreInternalTest.VALUE), PostgreInternalTest.LONG_VALUE)).thenReturn(updateSetMoreStep);
		when(this.updateSetMoreStep.where(field(FIELD_ID).eq(VERSION_NAME))).thenReturn(updateConditionStep);

		this.postgreInternal.updateVersion(PostgreInternalTest.LONG_VALUE);
	}
}
