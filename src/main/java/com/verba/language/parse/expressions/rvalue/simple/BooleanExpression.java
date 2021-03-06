package com.verba.language.parse.expressions.rvalue.simple;

import com.verba.language.build.configuration.Build;
import com.verba.language.emit.variables.VirtualVariable;
import com.verba.language.graph.expressions.functions.FunctionGraphVisitor;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.graph.visitors.ExpressionTreeVisitor;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.categories.LiteralExpression;
import com.verba.language.parse.expressions.categories.RValueExpression;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.identifiers.KeywordToken;
import com.verba.tools.exceptions.CompilerException;

/**
 * Created by sircodesalot on 14/12/9.
 */
public class BooleanExpression extends VerbaExpression implements LiteralExpression, RValueExpression {
  private boolean value;

  public BooleanExpression(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    this.value = determineValue(lexer);
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

  private boolean determineValue(Lexer lexer) {
    if (lexer.currentIs(KeywordToken.class, KeywordToken.TRUE)) {
      lexer.readCurrentAndAdvance(KeywordToken.class, KeywordToken.TRUE);
      return true;
    } else if (lexer.currentIs(KeywordToken.class, KeywordToken.FALSE)) {
      lexer.readCurrentAndAdvance(KeywordToken.class, KeywordToken.FALSE);
      return false;
    }

    throw new CompilerException("Invalid boolean value");
  }

  public boolean value() { return this.value; }

  public static BooleanExpression read(VerbaExpression parent, Lexer lexer) {
    return new BooleanExpression(parent, lexer);
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
