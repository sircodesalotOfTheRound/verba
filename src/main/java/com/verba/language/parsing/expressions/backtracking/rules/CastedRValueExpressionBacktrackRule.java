package com.verba.language.parsing.expressions.backtracking.rules;

import com.verba.language.parsing.expressions.VerbaExpression;
import com.verba.language.parsing.expressions.backtracking.BacktrackRule;
import com.verba.language.parsing.expressions.rvalue.cast.CastedRValueExpression;
import com.verba.language.parsing.lexing.Lexer;
import com.verba.language.parsing.info.LexList;

/**
 * Created by sircodesalot on 14-2-27.
 */
public class CastedRValueExpressionBacktrackRule extends BacktrackRule {
  @Override
  @Deprecated
  public boolean attemptIf(VerbaExpression parent, Lexer lexer, LexList restOfLine) {
    return false;
  }

  @Override
  public VerbaExpression attempt(VerbaExpression parent, Lexer lexer, LexList restOfLine) {
    return CastedRValueExpression.read(parent, lexer);
  }
}
