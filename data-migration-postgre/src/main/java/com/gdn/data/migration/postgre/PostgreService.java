package com.gdn.data.migration.postgre;

import org.jooq.DSLContext;

/**
 * Service JOOQ for PostgreSQL
 *
 * @author Eko Kurniawan Khannedy
 * @since 20/10/16
 */
public class PostgreService {

  private DSLContext context;

  public PostgreService(DSLContext context) {
    this.context = context;
  }

  /**
   * Get DSL Context for PostgreSQL
   *
   * @return DSLContext
   */
  public DSLContext getContext() {
    return context;
  }
}
