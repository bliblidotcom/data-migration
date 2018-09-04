package com.gdn.data.migration.mongo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.gdn.data.migration.core.DataMigrationProperties;
import org.bson.conversions.Bson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.gdn.data.migration.core.Internal;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoInternalTest {

  private static final Long VERSION_0 = 0L;
  private static final Long VERSION_1 = 1L;
  private static final Long VERSION_2 = 2L;

  private MongoInternal mongoInternal;
  private BasicDBObject version0;
  private BasicDBObject version1;
  private BasicDBObject version2;

  @Mock
  private MongoDatabase mongoDatabase;

  @Mock
  private MongoCollection<BasicDBObject> versionCollection;

  @Mock
  FindIterable<BasicDBObject> listVersion;
  
  private DataMigrationProperties dataMigrationProperties = new DataMigrationProperties();

  @Test
  public void currentVersionTest() {
    when(listVersion.first()).thenReturn(version1);
    when(versionCollection.find(new BasicDBObject("_id", dataMigrationProperties.getVersionTableName()))).thenReturn(
        listVersion);
    when(mongoDatabase.getCollection(dataMigrationProperties.getVersionTableName(), BasicDBObject.class)).thenReturn(
        versionCollection);
    mongoInternal = new MongoInternal(mongoDatabase, dataMigrationProperties);

    Long currentVersion = this.mongoInternal.currentVersion();

    verify(mongoDatabase).getCollection(dataMigrationProperties.getVersionTableName(), BasicDBObject.class);
    verify(versionCollection).find(new BasicDBObject("_id", dataMigrationProperties.getVersionTableName()));
    verify(listVersion).first();
    assertEquals(currentVersion, VERSION_1);
  }

  @Test
  public void ensureVersionEmptyTest() {
    when(versionCollection.count()).thenReturn(0L);
    when(mongoDatabase.getCollection(dataMigrationProperties.getVersionTableName(), BasicDBObject.class)).thenReturn(
        versionCollection);
    mongoInternal = new MongoInternal(mongoDatabase, dataMigrationProperties);

    this.mongoInternal.ensureVersion();

    verify(mongoDatabase).getCollection(dataMigrationProperties.getVersionTableName(), BasicDBObject.class);
    verify(versionCollection).count();
    verify(versionCollection).insertOne(version0);
  }


  @Test
  public void ensureVersionTest() {
    when(versionCollection.count()).thenReturn(2L);
    when(mongoDatabase.getCollection(dataMigrationProperties.getVersionTableName(), BasicDBObject.class)).thenReturn(
        versionCollection);
    mongoInternal = new MongoInternal(mongoDatabase, dataMigrationProperties);

    this.mongoInternal.ensureVersion();

    verify(mongoDatabase).getCollection(dataMigrationProperties.getVersionTableName(), BasicDBObject.class);
    verify(versionCollection).count();
    verify(versionCollection, times(0)).insertOne(version0);
  }


  @Before
  public void setup() {
    initMocks(this);

    version0 = new BasicDBObject();
    version0.put("_id", dataMigrationProperties.getVersionTableName());
    version0.put("value", 0L);

    version1 = new BasicDBObject();
    version1.put("_id", dataMigrationProperties.getVersionTableName());
    version1.put("value", 1L);

  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(mongoDatabase);
    verifyNoMoreInteractions(versionCollection);
    verifyNoMoreInteractions(listVersion);
  }

  @Test
  public void updateVersionTest() {
    when(mongoDatabase.getCollection(dataMigrationProperties.getVersionTableName(), BasicDBObject.class)).thenReturn(
        versionCollection);
    mongoInternal = new MongoInternal(mongoDatabase, dataMigrationProperties);

    this.mongoInternal.updateVersion(VERSION_1);

    BasicDBObject object = new BasicDBObject();
    object.put("value", VERSION_1);
    verify(mongoDatabase).getCollection(dataMigrationProperties.getVersionTableName(), BasicDBObject.class);
    verify(versionCollection).updateOne(any(Bson.class), eq(new BasicDBObject("$set", object)));
  }
}
