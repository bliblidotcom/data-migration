package com.gdn.data.migration.postgre;

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

  public PostgreInternal(PostgreService postgreService) {
    this.postgreService = postgreService;
  }

  @Override
  public void ensureVersion() {
    postgreService.getContext()
        .createTableIfNotExists(VERSION_NAME)
        .column("id", SQLDataType.VARCHAR)
        .column("value", SQLDataType.BIGINT)
        .constraints(
            constraint("pk_blibli_migration_version").primaryKey("id"))
        .execute();

    if (postgreService.getContext().fetchCount(table(name(VERSION_NAME))) == 0) {
      postgreService.getContext().insertInto(table(name(VERSION_NAME)))
          .set(field("id"), VERSION_NAME)
          .set(field("value"), 0L)
          .execute();
    }
  }

  @Override
  public void updateVersion(Long version) {
    postgreService.getContext()
        .update(table(name(VERSION_NAME)))
        .set(field("value"), version)
        .where(field("id").eq(VERSION_NAME))
        .execute();
  }

  @Override
  public Long currentVersion() {
    return postgreService.getContext()
        .select(field("value", SQLDataType.BIGINT))
        .from(VERSION_NAME)
        .where(field("id").eq(VERSION_NAME))
        .fetchOne()
        .value1();
  }
}
