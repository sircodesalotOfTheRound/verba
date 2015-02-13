package com.verba.language.parse.expressions.containers.set;

import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;
import com.verba.language.graph.visitors.ExpressionTreeVisitor;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.categories.DataContainerExpression;
import com.verba.language.parse.expressions.categories.RValueExpression;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.operators.enclosure.EnclosureToken;
import com.verba.language.parse.tokens.operators.mathop.OperatorToken;

/**
 * Created by sircodesalot on 14-5-21.
 */
public class SetDeclarationExpression extends VerbaExpression
  implements DataContainerExpression<VerbaExpression>, RValueExpression {
  private final QList<VerbaExpression> items = new QList<>();

  private SetDeclarationExpression(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    lexer.readCurrentAndAdvance(EnclosureToken.class, "{");

    while (lexer.notEOF()
      && !lexer.currentIs(EnclosureToken.class, "}")
      && !lexer.currentIs(OperatorToken.class, ",")) {

      RValueExpression item = RValueExpression.read(this, lexer);
      this.items.add((VerbaExpression) item);

      if (lexer.currentIs(OperatorToken.class, ",")) lexer.readCurrentAndAdvance(OperatorToken.class, ",");
      else break; // Shennanigans
    }

    lexer.readCurrentAndAdvance(EnclosureToken.class, "}");
    this.closeLexingRegion();
  }

  @Override
  protected void onChildRemoved(VerbaExpression child) {

  }

  public QIterable<VerbaExpression> items() {
    return this.items;
  }

  public static SetDeclarationExpression read(VerbaExpression expression, Lexer lexer) {
    return new SetDeclarationExpression(expression, lexer);
  }

  @Override
  public void accept(ExpressionTreeVisitor visitor) {

  }
}
