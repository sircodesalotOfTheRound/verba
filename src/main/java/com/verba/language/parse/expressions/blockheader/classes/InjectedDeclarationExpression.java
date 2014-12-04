package com.verba.language.parse.expressions.blockheader.classes;

import com.verba.language.graph.symbols.table.tables.ScopedSymbolTable;
import com.verba.language.graph.visitors.SyntaxGraphVisitor;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.block.BlockDeclarationExpression;
import com.verba.language.parse.expressions.blockheader.NamedBlockExpression;
import com.verba.language.parse.expressions.blockheader.generic.GenericTypeListExpression;
import com.verba.language.parse.expressions.members.FullyQualifiedNameExpression;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.identifiers.KeywordToken;

/**
 * Created by sircodesalot on 14-2-17.
 */
public class InjectedDeclarationExpression extends VerbaExpression implements NamedBlockExpression {
  private final FullyQualifiedNameExpression identifier;
  private final BlockDeclarationExpression block;
  private final GenericTypeListExpression genericParameters;

  public InjectedDeclarationExpression(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    lexer.readCurrentAndAdvance(KeywordToken.class, "injected");
    this.identifier = FullyQualifiedNameExpression.read(this, lexer);
    this.genericParameters = GenericTypeListExpression.read(this, lexer);

    this.block = BlockDeclarationExpression.read(this, lexer);
    this.closeLexingRegion();
  }

  public static InjectedDeclarationExpression read(VerbaExpression parent, Lexer lexer) {
    return new InjectedDeclarationExpression(parent, lexer);
  }

  public FullyQualifiedNameExpression identifier() {
    return this.identifier;
  }

  public BlockDeclarationExpression block() {
    return this.block;
  }

  public GenericTypeListExpression genericParameters() {
    return this.genericParameters;
  }

  @Override
  public String name() {
    return this.identifier.members().first().memberName();
  }

  @Override
  public void accept(SyntaxGraphVisitor visitor) {

  }

  @Override
  public void accept(ScopedSymbolTable symbolTable) {

  }
}