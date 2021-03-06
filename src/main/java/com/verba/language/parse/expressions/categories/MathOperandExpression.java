package com.verba.language.parse.expressions.categories;

import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.backtracking.BacktrackRuleSet;
import com.verba.language.parse.expressions.backtracking.rules.InfixExpressionBacktrackRule;
import com.verba.language.parse.lexing.Lexer;

/**
 * Created by sircodesalot on 14/9/18.
 */
public interface MathOperandExpression extends ExpressionCategory {
  public static BacktrackRuleSet<MathOperandExpression> ruleset = new BacktrackRuleSet<>(RValueExpression.rvalueRuleSet
    .where(rule -> !(rule instanceof InfixExpressionBacktrackRule)));

  public static MathOperandExpression read(VerbaExpression parent, Lexer lexer) {
    return ruleset.resolve(parent, lexer);
  }
}
