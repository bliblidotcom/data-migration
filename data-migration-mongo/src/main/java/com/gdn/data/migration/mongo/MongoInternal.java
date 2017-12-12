package com.gdn.data.migration.mongo;

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

  public MongoInternal(MongoDatabase mongoDatabase) {
    this.versionCollection = mongoDatabase.getCollection(VERSION_NAME, BasicDBObject.class);
  }

  @Override
  public void ensureVersion() {
    if (versionCollection.count() == 0) {
      BasicDBObject object = new BasicDBObject();
      object.put("_id", VERSION_NAME);
      object.put("value", 0L);

      versionCollection.insertOne(object);
    }
  }

  @Override
  public void updateVersion(Long version) {
    BasicDBObject object = new BasicDBObject();
    object.put("value", version);

    versionCollection.updateOne(
        Filters.eq("_id", VERSION_NAME),
        new BasicDBObject("$set", object)
    );
  }

  @Override
  public Long currentVersion() {
    return versionCollection.find(new BasicDBObject("_id", VERSION_NAME))
        .first().getLong("value");
  }

}
