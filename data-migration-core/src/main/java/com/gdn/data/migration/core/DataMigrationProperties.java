package com.gdn.data.migration.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ConfigurationProperties("data.migration")
public class DataMigrationProperties {

  private String versionTableName = "blibli_migration_version";
}
