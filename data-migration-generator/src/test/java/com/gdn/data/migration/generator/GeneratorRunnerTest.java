package com.gdn.data.migration.generator;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

/**
 * Created by lukman.h on 12/9/2016.
 */
public class GeneratorRunnerTest {

  private static final String BLANK = "";
  private static final String MIGRATION = "Migration";

  @InjectMocks
  private GeneratorRunner generatorRunner;

  @Mock
  private Handlebars handlebars;
  @Mock
  private Template template;
  @Mock
  private GeneratorConfiguration configuration;

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  @Test
  public void runTest() {
    try {
      generatorRunner.run();
      verify(handlebars).compile(MIGRATION);
      verify(template).apply(any(Map.class));
      verify(configuration).getTarget();
      verify(configuration).getPrefix();
      verify(configuration).getSuffix();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Before
  public void setUp() {
    initMocks(this);
    generatorRunner = new GeneratorRunner(handlebars, configuration);
    try {
      when(configuration.getTarget()).thenReturn(tempFolder.getRoot().getAbsolutePath());
      when(template.apply(any(Map.class))).thenReturn(BLANK);
      when(handlebars.compile(MIGRATION)).thenReturn(template);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(handlebars);
    verifyNoMoreInteractions(template);
    verifyNoMoreInteractions(configuration);
  }

}
