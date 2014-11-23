package com.verba.language.parsing.expressions.backtracking.rules;

import com.verba.language.parsing.lexing.Lexer;
import com.verba.language.parsing.expressions.VerbaExpression;
import com.verba.language.parsing.expressions.backtracking.BacktrackRule;
import com.verba.language.parsing.expressions.blockheader.functions.TaskDeclarationExpression;
import com.verba.language.parsing.info.LexList;
import com.verba.language.parsing.tokens.identifiers.KeywordToken;

/**
 * Created by sircodesalot on 14-2-22.
 */
public class TaskDeclarationBacktrackRule extends BacktrackRule {
  @Override
  public boolean attemptIf(VerbaExpression parent, Lexer lexer, LexList restOfLine) {
    return restOfLine.startsWith(KeywordToken.class, "task");
  }

  @Override
  public VerbaExpression attempt(VerbaExpression parent, Lexer lexer, LexList restOfLine) {
    return TaskDeclarationExpression.read(parent, lexer);
  }
}
