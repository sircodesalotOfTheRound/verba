package com.verba.language.parse.expressions.containers.markup;

import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;
import com.verba.language.graph.visitors.SyntaxGraphVisitor;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.categories.MarkupTagExpression;
import com.verba.language.parse.expressions.categories.RValueExpression;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.operators.mathop.OperatorToken;

/**
 * Created by sircodesalot on 14-5-22.
 */
public class MarkupDeclarationExpression extends VerbaExpression
  implements MarkupTagExpression, RValueExpression {

  private final QIterable<VerbaExpression> tags;

  private MarkupDeclarationExpression(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    this.tags = this.readAllTags(lexer);
    this.closeLexingRegion();
  }

  private QIterable<VerbaExpression> readAllTags(Lexer lexer) {
    QList<VerbaExpression> tags = new QList<>();

    while (lexer.notEOF() && lexer.currentIs(OperatorToken.class, "<")) {
      MarkupTagItemExpression tag = MarkupTagItemExpression.read(this, lexer);
      tags.add(tag);
    }

    return tags;
  }

  @Override
  public QIterable<VerbaExpression> items() {
    return this.tags;
  }

  public static MarkupDeclarationExpression read(VerbaExpression parent, Lexer lexer) {
    return new MarkupDeclarationExpression(parent, lexer);
  }

  @Override
  public void accept(SyntaxGraphVisitor visitor) {

  }
}