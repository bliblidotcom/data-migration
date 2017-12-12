package com.gdn.data.migration.generator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Eko Kurniawan Khannedy
 */
@Component
public class GeneratorConfiguration {

  private String target = "src/main/java/migrations/";

  private String prefix = "Migration_";

  private String suffix = ".java";

  public String getTarget() {
    return target;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getSuffix() {
    return suffix;
  }
}
