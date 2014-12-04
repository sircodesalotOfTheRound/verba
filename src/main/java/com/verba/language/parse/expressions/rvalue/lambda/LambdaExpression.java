package com.verba.language.parse.expressions.rvalue.lambda;

import com.verba.language.graph.visitors.SyntaxGraphVisitor;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.categories.RValueExpression;
import com.verba.language.parse.expressions.categories.TypeDeclarationExpression;
import com.verba.language.parse.expressions.members.FullyQualifiedNameExpression;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.lambda.LambdaToken;

/**
 * Created by sircodesalot on 14-2-28.
 */
public class LambdaExpression extends VerbaExpression implements RValueExpression {
  private TypeDeclarationExpression lvalue;
  private RValueExpression rvalue;

  public LambdaExpression(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    this.lvalue = TypeDeclarationExpression.read(this, lexer);

    lexer.readCurrentAndAdvance(LambdaToken.class);

    // Attempt to read RValueExpression
    this.rvalue = RValueExpression.read(this, lexer);
    if (this.rvalue == null) {
      FullyQualifiedNameExpression.read(this, lexer);
    }

    this.closeLexingRegion();
  }

  public static LambdaExpression read(VerbaExpression parent, Lexer lexer) {
    return new LambdaExpression(parent, lexer);
  }

  public TypeDeclarationExpression lvalue() {
    return this.lvalue;
  }

  public RValueExpression rvalue() {
    return this.rvalue;
  }

  @Override
  public void accept(SyntaxGraphVisitor visitor) {

  }
}