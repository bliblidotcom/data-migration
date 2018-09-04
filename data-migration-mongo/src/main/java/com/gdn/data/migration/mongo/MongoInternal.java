package com.gdn.data.migration.mongo;

import com.gdn.data.migration.core.DataMigrationProperties;
import com.gdn.data.migration.core.Internal;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author Eko Kurniawan Khannedy
 * @since 20/10/16
 */
public class MongoInternal implements Internal {

  private MongoCollection<BasicDBObject> versionCollection;

  private DataMigrationProperties dataMigrationProperties;

  public MongoInternal(MongoDatabase mongoDatabase, DataMigrationProperties dataMigrationProperties) {
    this.dataMigrationProperties = dataMigrationProperties;
    this.versionCollection = mongoDatabase.getCollection(dataMigrationProperties.getVersionTableName(), BasicDBObject.class);
  }

  @Override
  public void ensureVersion() {
    if (versionCollection.count() == 0) {
      BasicDBObject object = new BasicDBObject();
      object.put("_id", dataMigrationProperties.getVersionTableName());
      object.put("value", 0L);

      versionCollection.insertOne(object);
    }
  }

  @Override
  public void updateVersion(Long version) {
    BasicDBObject object = new BasicDBObject();
    object.put("value", version);

    versionCollection.updateOne(
        Filters.eq("_id", dataMigrationProperties.getVersionTableName()),
        new BasicDBObject("$set", object)
    );
  }

  @Override
  public Long currentVersion() {
    return versionCollection.find(new BasicDBObject("_id", dataMigrationProperties.getVersionTableName()))
        .first().getLong("value");
  }

}
