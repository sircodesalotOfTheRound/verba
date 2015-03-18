package com.verba.types;

import com.verba.language.parse.expressions.primitives.PlatformTypeExpression;
import com.verba.language.parse.tokens.identifiers.KeywordToken;
import org.junit.Test;

/**
 * Created by sircodesalot on 15/3/17.
 */
public class TestPlatformTypes {
  @Test
  public void test() {
    // Platform type expression will fail if the type is invalid.
    for (String keyword : KeywordToken.platformTypeKeywords()) {
      PlatformTypeExpression expression = new PlatformTypeExpression(keyword);
    }
  }
}