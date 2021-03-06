package com.verba.language.graph.expressions.functions.variables;

import com.verba.language.parse.expressions.VerbaExpression;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by sircodesalot on 14/9/21.
 */
public class VariableLifetime {
  private final VerbaExpression expression;
  private final int beginningLineNumber;
  private int endingLineNumber;

  public VariableLifetime(VerbaExpression expression) {
    this.expression = expression;
    this.beginningLineNumber = expression.startingLine();
    this.endingLineNumber = beginningLineNumber;
  }

  public VerbaExpression expression() { return expression; }
  public int beginningLineNumber() { return beginningLineNumber; }
  public int endingLineNumber() { return endingLineNumber; }
  public boolean isTemporary() { return beginningLineNumber == endingLineNumber; }

  public void updateEndingLineNumber(VerbaExpression expression) {
    validateExpression(expression);

    this.endingLineNumber = expression.endingLine();
  }

  private void validateExpression(VerbaExpression expression) {
    if (!this.expression.text().equals(expression.text())) {
      throw new NotImplementedException();
    }
  }

  public boolean isFirstInstance(VerbaExpression expression) {
    validateExpression(expression);

    return (expression.startingLine() == beginningLineNumber);
  }

  public boolean isLastOccurance(VerbaExpression expression) {
    validateExpression(expression);

    return (expression.endingLine() == endingLineNumber);
  }
}
