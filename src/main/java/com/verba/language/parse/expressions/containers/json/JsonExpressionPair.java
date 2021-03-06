package com.verba.language.parse.expressions.containers.json;

import com.verba.language.build.configuration.Build;
import com.verba.language.emit.variables.VirtualVariable;
import com.verba.language.graph.expressions.functions.FunctionGraphVisitor;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.graph.visitors.ExpressionTreeVisitor;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.categories.RValueExpression;
import com.verba.language.parse.expressions.rvalue.simple.IdentifierExpression;
import com.verba.language.parse.expressions.rvalue.simple.UtfExpression;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.identifiers.IdentifierToken;
import com.verba.language.parse.tokens.literals.UtfToken;
import com.verba.language.parse.tokens.operators.mathop.OperatorToken;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by sircodesalot on 14-2-24.
 */
public class JsonExpressionPair extends VerbaExpression {
  private VerbaExpression lhs;
  private RValueExpression rhs;

  private JsonExpressionPair(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    this.lhs = this.readLhsItem(lexer);
    lexer.readCurrentAndAdvance(OperatorToken.class, ":");
    this.rhs = RValueExpression.read(this, lexer);
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

  private VerbaExpression readLhsItem(Lexer lexer) {
    if (lexer.currentIs(IdentifierToken.class)) return IdentifierExpression.read(this, lexer);
    else if (lexer.currentIs(UtfToken.class)) return UtfExpression.read(this, lexer);

    throw new NotImplementedException();
  }

  public static JsonExpressionPair read(VerbaExpression parent, Lexer lexer) {
    return new JsonExpressionPair(parent, lexer);
  }

  public VerbaExpression lhs() {
    return this.lhs;
  }

  public RValueExpression rhs() {
    return this.rhs;
  }

  @Override
  public String toString() {
    return String.format("%s : %s", this.lhs, this.rhs);
  }

  @Override
  public void accept(ExpressionTreeVisitor visitor) {

  }

  @Override
  public VirtualVariable accept(FunctionGraphVisitor visitor) {
    return null;
  }
}
