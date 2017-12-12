package com.gdn.data.migration.generator;

import com.gdn.data.migration.core.Runner;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Runner for create new migration script
 *
 * @author Eko Kurniawan Khannedy
 * @since 19/10/16
 */
@Component
public class GeneratorRunner implements Runner {

  private static final Logger LOG = LoggerFactory.getLogger(GeneratorRunner.class);

  private Handlebars handlebars;

  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

  private GeneratorConfiguration configuration;

  @Autowired
  public GeneratorRunner(Handlebars handlebars, GeneratorConfiguration configuration) {
    this.handlebars = handlebars;
    this.configuration = configuration;
  }

  @Override
  public void run() throws Exception {
    LOG.info("Load migration template");
    Template template = handlebars.compile("Migration");

    LOG.info("Create migration version");
    String version = dateFormat.format(new Date());
    Map<String, Object> data = new HashMap<>();
    data.put("version", version);

    LOG.info("Fill migration template");
    String result = template.apply(data);
    File file = getGeneratedFile(version);
    IOUtils.write(result, new FileOutputStream(file), Charset.defaultCharset());
    LOG.info("Success create migration file {}", file);
  }

  /**
   * Create generated file
   *
   * @param version version
   * @return generated file
   */
  public File getGeneratedFile(String version) {
    return new File(configuration.getTarget() + configuration.getPrefix() + version + configuration.getSuffix());
  }
}
