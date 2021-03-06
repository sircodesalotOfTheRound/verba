package com.verba.language.parse.expressions.containers.markup;

import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;
import com.verba.language.build.configuration.Build;
import com.verba.language.emit.variables.VirtualVariable;
import com.verba.language.graph.expressions.functions.FunctionGraphVisitor;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.graph.visitors.ExpressionTreeVisitor;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.categories.MarkupTagExpression;
import com.verba.language.parse.expressions.categories.RValueExpression;
import com.verba.language.parse.expressions.members.FullyQualifiedNameExpression;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.identifiers.IdentifierToken;
import com.verba.language.parse.tokens.identifiers.KeywordToken;
import com.verba.language.parse.tokens.operators.enclosure.EnclosureToken;
import com.verba.language.parse.tokens.operators.mathop.OperatorToken;

/**
 * Created by sircodesalot on 14-5-22.
 */
public class MarkupDeclarationExpression extends VerbaExpression
  implements MarkupTagExpression, RValueExpression {

  private final QIterable<MarkupTagItemExpression> tags;
  private final FullyQualifiedNameExpression name;

  private MarkupDeclarationExpression(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    lexer.readCurrentAndAdvance(KeywordToken.class, KeywordToken.MARKUP);
    this.name = determineName(lexer);

    lexer.readCurrentAndAdvance(EnclosureToken.class, "{");
    this.tags = this.readAllTags(lexer);
    lexer.readCurrentAndAdvance(EnclosureToken.class, "}");

    this.closeLexingRegion();
  }

  @Override
  protected void onChildRemoved(VerbaExpression child) {

  }

  @Override
  public void afterContentsParsed(Build build) {

  }

  @Override
  public void afterSymbolsGenerated(Build build, SymbolTable table) {

  }

  @Override
  public void onResolveSymbols(Build build, SymbolTable table) {

  }

  @Override
  public void onValidate(Build build, SymbolTable table) {

  }

  private FullyQualifiedNameExpression determineName(Lexer lexer) {
    if (lexer.currentIs(IdentifierToken.class)) {
      return FullyQualifiedNameExpression.read(this, lexer);
    } else {
      return null;
    }
  }

  private QIterable<MarkupTagItemExpression> readAllTags(Lexer lexer) {
    QList<MarkupTagItemExpression> tags = new QList<>();

    while (lexer.notEOF() && lexer.currentIs(OperatorToken.class, "<")) {
      MarkupTagItemExpression tag = MarkupTagItemExpression.read(this, lexer);
      tags.add(tag);
    }

    return tags;
  }

  @Override
  public QIterable<MarkupTagItemExpression> items() {
    return this.tags;
  }

  public static MarkupDeclarationExpression read(VerbaExpression parent, Lexer lexer) {
    return new MarkupDeclarationExpression(parent, lexer);
  }

  @Override
  public void accept(ExpressionTreeVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public VirtualVariable accept(FunctionGraphVisitor visitor) {
    return null;
  }
}
