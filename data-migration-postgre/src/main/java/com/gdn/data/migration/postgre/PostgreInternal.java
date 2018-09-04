package com.gdn.data.migration.postgre;

import com.gdn.data.migration.core.DataMigrationProperties;
import com.gdn.data.migration.core.Internal;
import org.jooq.impl.SQLDataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.jooq.impl.DSL.*;

/**
 * @author Eko Kurniawan Khannedy
 * @since 20/10/16
 */
public class PostgreInternal implements Internal {

  private PostgreService postgreService;

  private DataMigrationProperties dataMigrationProperties;

  public PostgreInternal(PostgreService postgreService, DataMigrationProperties dataMigrationProperties) {
    this.postgreService = postgreService;
    this.dataMigrationProperties = dataMigrationProperties;
  }

  @Override
  public void ensureVersion() {
    postgreService.getContext()
        .createTableIfNotExists(dataMigrationProperties.getVersionTableName())
        .column("id", SQLDataType.VARCHAR)
        .column("value", SQLDataType.BIGINT)
        .constraints(
            constraint("pk_blibli_migration_version").primaryKey("id"))
        .execute();

    if (postgreService.getContext().fetchCount(table(name(dataMigrationProperties.getVersionTableName()))) == 0) {
      postgreService.getContext().insertInto(table(name(dataMigrationProperties.getVersionTableName())))
          .set(field("id"), dataMigrationProperties.getVersionTableName())
          .set(field("value"), 0L)
          .execute();
    }
  }

  @Override
  public void updateVersion(Long version) {
    postgreService.getContext()
        .update(table(name(dataMigrationProperties.getVersionTableName())))
        .set(field("value"), version)
        .where(field("id").eq(dataMigrationProperties.getVersionTableName()))
        .execute();
  }

  @Override
  public Long currentVersion() {
    return postgreService.getContext()
        .select(field("value", SQLDataType.BIGINT))
        .from(dataMigrationProperties.getVersionTableName())
        .where(field("id").eq(dataMigrationProperties.getVersionTableName()))
        .fetchOne()
        .value1();
  }
}
