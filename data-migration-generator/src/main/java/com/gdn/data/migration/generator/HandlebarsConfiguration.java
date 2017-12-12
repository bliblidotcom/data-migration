package com.gdn.data.migration.generator;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Eko Kurniawan Khannedy
 * @since 19/10/16
 */
@Configuration
public class HandlebarsConfiguration {

  /**
   * Create handlebars bean
   *
   * @param templateLoader template loader
   * @return handlebars
   */
  @Bean
  public Handlebars handlebars(TemplateLoader templateLoader) {
    return new Handlebars(templateLoader);
  }

  /**
   * Create template loader bean
   *
   * @return template loader
   */
  @Bean
  public TemplateLoader templateLoader() {
    return new ClassPathTemplateLoader("/templates", ".txt");
  }

}
