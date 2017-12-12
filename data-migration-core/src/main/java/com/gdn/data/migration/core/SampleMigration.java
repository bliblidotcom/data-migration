package com.gdn.data.migration.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Your Team
 */
//@Component
public class SampleMigration implements Migration {

  private static Logger LOG = LoggerFactory.getLogger(SampleMigration.class);

  @Override
  public Long version() {
    return null;
  }

  @Override
  public String name() {
    return null;
  }

  // @Autowired
  // private MongoConfiguration mongoConfiguration;

  // @Autowired
  // private PostgreConfiguration postgreConfiguration;

  // @Autowired
  // private ElasticsearchConfiguration elasticsearchConfiguration;

  // @Autowired
  // private OkHttpClient okHttpClient;

  // @Autowired
  // private ObjectMapper objectMapper;

  // @Autowired
  // private PostgreService postgreService;

  // @Autowired
  // private MongoDatabase mongoDatabase;

  @Override
  public void migrate() {
    // Migration script
  }

  @Override
  public void rollback() {
    // Rollback script
  }
}
