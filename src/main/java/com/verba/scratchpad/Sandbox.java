package com.verba.scratchpad;

import com.verba.language.build.Build;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.blockheader.functions.FunctionDeclarationExpression;

/**
 * Created by sircodesalot on 14-2-16.
 */
public class Sandbox {
  public static void main(String[] args) throws Exception {
    Build build = Build.fromString(true, "fn my_function { val item = \"something\" val another = \"another\"}");

    FunctionDeclarationExpression my_function = build.symbolTable()
      .findAllMatchingFqn("my_function")
      .single()
      .expressionAs(FunctionDeclarationExpression.class);

    System.out.println(my_function.returnType().fqn());
  }
}
