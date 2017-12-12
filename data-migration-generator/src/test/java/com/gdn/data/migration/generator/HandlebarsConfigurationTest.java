package com.gdn.data.migration.generator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

/**
 * Created by lukman.h on 12/10/2016.
 */
public class HandlebarsConfigurationTest {

  private static final String PREFIX = "/templates";
  private static final String SUFFIX = ".txt";

  private HandlebarsConfiguration handlebarsConfiguration;
  private TemplateLoader templateLoader;

  @Test
  public void gethandlebarsTest() {
    Handlebars result = handlebarsConfiguration.handlebars(templateLoader);
    assertThat(result.getLoader().getPrefix(), equalTo(templateLoader.getPrefix()));
  }

  @Test
  public void getTemplateLoaderTest() {
    TemplateLoader result = handlebarsConfiguration.templateLoader();
    assertThat(result.getPrefix(), equalTo(templateLoader.getPrefix()));
  }

  @Before
  public void setUp() {
    handlebarsConfiguration = new HandlebarsConfiguration();
    templateLoader = new ClassPathTemplateLoader(PREFIX, SUFFIX);
  }

}
