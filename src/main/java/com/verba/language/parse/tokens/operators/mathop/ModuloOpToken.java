package com.verba.language.parse.tokens.operators.mathop;

/**
 * Created by sircodesalot on 14-2-27.
 */
public class ModuloOpToken extends InfixOperatorToken {

  public ModuloOpToken() {
    super("%");
  }

  @Override
  public int getPriorityLevel() {
    return 2;
  }
}
