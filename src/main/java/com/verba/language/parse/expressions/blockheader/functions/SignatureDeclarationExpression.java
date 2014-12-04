package com.verba.language.parse.expressions.blockheader.functions;

import com.verba.language.graph.symbols.table.tables.ScopedSymbolTable;
import com.verba.language.graph.visitors.SyntaxGraphVisitor;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.blockheader.generic.GenericTypeListExpression;
import com.verba.language.parse.expressions.categories.*;
import com.verba.language.parse.expressions.members.FullyQualifiedNameExpression;
import com.verba.language.parse.expressions.members.MemberExpression;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.identifiers.KeywordToken;
import com.verba.language.parse.tokens.operators.mathop.OperatorToken;

/**
 * Created by sircodesalot on 14-2-17.
 */
// TODO: Merge with function declaration expression.
@Deprecated
public class SignatureDeclarationExpression extends VerbaExpression implements NamedExpression,
  TypedExpression, GenericallyParameterizedExpression, SymbolTableExpression {

  private final FullyQualifiedNameExpression identifier;
  private TypeDeclarationExpression returnType;

  public SignatureDeclarationExpression(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    lexer.readCurrentAndAdvance(KeywordToken.class, "signature");
    this.identifier = FullyQualifiedNameExpression.read(this, lexer);

    if (lexer.currentIs(OperatorToken.class, ":")) {
      lexer.readCurrentAndAdvance(OperatorToken.class, ":");
      this.returnType = TypeDeclarationExpression.read(this, lexer);
    }

    this.closeLexingRegion();
  }

  public static SignatureDeclarationExpression read(VerbaExpression parent, Lexer lexer) {
    return new SignatureDeclarationExpression(parent, lexer);
  }


  public boolean hasGenericParameters() {
    return this.primaryIdentifier().hasGenericParameters();
  }

  public boolean hasParameters() {
    return (this.primaryIdentifier().hasParameters());
  }

  public MemberExpression primaryIdentifier() {
    return this.declaration().first();
  }

  public FullyQualifiedNameExpression declaration() {
    return this.identifier;
  }

  public GenericTypeListExpression genericParameters() {
    return this.primaryIdentifier().genericParameterList();
  }

  @Override
  public String name() {
    return this.primaryIdentifier().memberName();
  }

  @Override
  public boolean hasTypeConstraint() {
    return this.returnType != null;
  }

  @Override
  public TypeDeclarationExpression typeConstraint() {
    return this.returnType;
  }

  @Override
  public void accept(SyntaxGraphVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public void accept(ScopedSymbolTable symbolTable) {
    symbolTable.visit(this);
  }
}