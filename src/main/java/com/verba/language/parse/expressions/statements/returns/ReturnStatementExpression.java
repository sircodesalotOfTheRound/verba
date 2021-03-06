package com.verba.language.parse.expressions.statements.returns;

import com.verba.language.build.configuration.Build;
import com.verba.language.emit.variables.VirtualVariable;
import com.verba.language.graph.expressions.functions.FunctionGraphVisitor;
import com.verba.language.graph.symbols.resolution.ReturnStatementTypeResolver;
import com.verba.language.graph.symbols.table.entries.Symbol;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.graph.visitors.ExpressionTreeVisitor;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.categories.RValueExpression;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.identifiers.KeywordToken;

/**
 * Created by sircodesalot on 14-2-22.
 */

public class ReturnStatementExpression extends VerbaExpression
{
  private RValueExpression value;
  private final ReturnStatementTypeResolver returnTypeResolver;

  public ReturnStatementExpression(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    this.returnTypeResolver = new ReturnStatementTypeResolver(this);

    int currentLine = lexer.current().line();
    lexer.readCurrentAndAdvance(KeywordToken.class, KeywordToken.RETURN);

    if (lexer.current().line() == currentLine) {
      this.value = RValueExpression.read(this, lexer);
    }

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
    this.returnTypeResolver.resolveReturnType(table);
  }

  @Override
  public void onResolveSymbols(Build build, SymbolTable table) {
    this.returnTypeResolver.resolveReturnType(table);
  }

  @Override
  public void onValidate(Build build, SymbolTable table) {

  }

  public static ReturnStatementExpression read(VerbaExpression expression, Lexer lexer) {
    return new ReturnStatementExpression(expression, lexer);
  }

  public RValueExpression value() {
    return this.value;
  }

  public boolean hasValue() {
    return this.value != null;
  }

  @Override
  public void accept(ExpressionTreeVisitor visitor) {
    visitor.visit(this);
  }

  public Symbol returnType() {
    return this.returnTypeResolver.returnType();
  }

  public Symbol returnType(SymbolTable table) {
    return this.returnTypeResolver.resolveReturnType(table);
  }

  @Override
  public VirtualVariable accept(FunctionGraphVisitor visitor) {
    return visitor.visit(this);
  }
}
