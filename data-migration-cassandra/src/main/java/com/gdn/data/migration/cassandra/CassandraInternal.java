package com.gdn.data.migration.cassandra;

import com.datastax.driver.core.*;
import com.gdn.data.migration.core.Internal;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CassandraInternal implements Internal {

  private Session session;

  private PreparedStatement prepareUpdateVersion;

  public CassandraInternal(Session session) {
    this.session = session;
  }

  public Long count() {
    ResultSet rows = session.execute("SELECT COUNT(*) FROM blibli_migration_version WHERE id = 'blibli_migration_version';");
    Row row = rows.one();

    if (row == null) {
      return 0L;
    } else {
      return row.getLong(0);
    }
  }

  @Override
  public void ensureVersion() {
    session.execute("CREATE TABLE IF NOT EXISTS blibli_migration_version " +
        "( id TEXT, version BIGINT, primary key (id) );");

    if (count() == 0L) {
      updateVersion(0L);
    }
  }

  @Override
  public void updateVersion(Long version) {
    if (prepareUpdateVersion == null) {
      prepareUpdateVersion = session.prepare("INSERT INTO blibli_migration_version (id, version) " +
          "VALUES ('blibli_migration_version', ?);");
    }

    BoundStatement statement = prepareUpdateVersion.bind(version);
    session.execute(statement);
  }

  @Override
  public Long currentVersion() {
    ResultSet rows = session.execute("SELECT * FROM blibli_migration_version WHERE id = 'blibli_migration_version'");
    Row row = rows.one();
    return row.getLong("version");
  }
}
