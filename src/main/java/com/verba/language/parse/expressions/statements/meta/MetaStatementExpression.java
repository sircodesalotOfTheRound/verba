package com.verba.language.parse.expressions.statements.meta;

import com.verba.language.build.configuration.Build;
import com.verba.language.emit.variables.VirtualVariable;
import com.verba.language.graph.expressions.functions.FunctionGraphVisitor;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.graph.visitors.ExpressionTreeVisitor;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.operators.mathop.OperatorToken;


/**
 * Created by sircodesalot on 14-2-28.
 */
public class MetaStatementExpression extends VerbaExpression {
  private final VerbaExpression statement;

  public MetaStatementExpression(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    lexer.readCurrentAndAdvance(OperatorToken.class, "@");
    this.statement = VerbaExpression.read(this, lexer);

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

  public static MetaStatementExpression read(VerbaExpression parent, Lexer lexer) {
    return new MetaStatementExpression(parent, lexer);
  }

  public VerbaExpression statement() {
    return this.statement;
  }

  @Override
  public void accept(ExpressionTreeVisitor visitor) {

  }

  @Override
  public VirtualVariable accept(FunctionGraphVisitor visitor) {
    return null;
  }
}
