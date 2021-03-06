package com.verba.language.parse.expressions.statements.flow.iteration;

import com.verba.language.build.configuration.Build;
import com.verba.language.emit.variables.VirtualVariable;
import com.verba.language.graph.expressions.functions.FunctionGraphVisitor;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.graph.visitors.ExpressionTreeVisitor;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.block.BlockDeclarationExpression;
import com.verba.language.parse.expressions.categories.BranchExpression;
import com.verba.language.parse.expressions.categories.RValueExpression;
import com.verba.language.parse.expressions.categories.TypeConstraintExpression;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.identifiers.KeywordToken;
import com.verba.language.parse.tokens.operators.enclosure.EnclosureToken;
import com.verba.language.parse.tokens.operators.mathop.OperatorToken;

/**
 * Created by sircodesalot on 14-2-25.
 */
public class ForStatementExpression extends VerbaExpression
  implements BranchExpression
{
  private TypeConstraintExpression variable;
  private RValueExpression spanExpression;
  private BlockDeclarationExpression block;

  private ForStatementExpression(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    this.readContent(lexer);
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

  private void readContent(Lexer lexer) {
    lexer.readCurrentAndAdvance(KeywordToken.class, KeywordToken.FOR);
    lexer.readCurrentAndAdvance(EnclosureToken.class, "(");

    this.variable = TypeConstraintExpression.read(this, lexer);

    lexer.readCurrentAndAdvance(OperatorToken.class, ":");
    this.spanExpression = RValueExpression.read(this, lexer);

    lexer.readCurrentAndAdvance(EnclosureToken.class, ")");
    this.block = BlockDeclarationExpression.read(this, lexer);

  }

  public static ForStatementExpression read(VerbaExpression parent, Lexer lexer) {
    return new ForStatementExpression(parent, lexer);
  }

  public TypeConstraintExpression variable() {
    return this.variable;
  }

  public RValueExpression spanExpression() {
    return this.spanExpression;
  }

  public BlockDeclarationExpression block() {
    return this.block;
  }

  @Override
  public void accept(ExpressionTreeVisitor visitor) {

  }

  @Override
  public VirtualVariable accept(FunctionGraphVisitor visitor) {
    return null;
  }
}
