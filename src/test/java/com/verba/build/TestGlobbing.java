package com.verba.build;

import com.javalinq.implementations.QSet;
import com.verba.language.build.configuration.Build;
import com.verba.language.build.configuration.BuildSpecification;
import com.verba.language.build.coordination.BuildManager;
import com.verba.language.build.infoitems.SourcesBuildItem;
import com.verba.language.build.steps.CollectSourcesBuildStep;
import org.junit.Test;

import java.io.File;

/**
 * Created by sircodesalot on 15/3/10.
 */
public class TestGlobbing {
  @Test
  public void testGlobbing() {
    BuildSpecification specification = new BuildSpecification()
      .addSourceFolder("verba_sources/glob_test");

    BuildManager build = specification.generateBuild();

    assert(build.containsBuildInfoOfType(SourcesBuildItem.class));

    QSet<String> filesAsSet = build
      .getBuildInfo(SourcesBuildItem.class)
      .files()
      .map(File::toString)
      .toSet();

    assert(filesAsSet.count() == 5);
    assert(filesAsSet.contains("verba_sources/glob_test/File1.v"));
    assert(filesAsSet.contains("verba_sources/glob_test/File2.v"));
    assert(filesAsSet.contains("verba_sources/glob_test/File3.v"));

    assert(filesAsSet.contains("verba_sources/glob_test/subfolder/File4.v"));
    assert(filesAsSet.contains("verba_sources/glob_test/subfolder/File5.v"));
  }
}
