package com.verba.language.parse.expressions.members;

import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;
import com.verba.language.build.configuration.Build;
import com.verba.language.emit.variables.VirtualVariable;
import com.verba.language.graph.expressions.functions.FunctionGraphVisitor;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.graph.visitors.ExpressionTreeVisitor;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.blockheader.generic.GenericTypeListExpression;
import com.verba.language.parse.expressions.containers.tuple.TupleDeclarationExpression;
import com.verba.language.parse.expressions.rvalue.simple.IdentifierExpression;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.identifiers.IdentifierToken;
import com.verba.language.parse.tokens.operators.enclosure.EnclosureToken;

/**
 * Created by sircodesalot on 14-2-25.
 */
public class MemberExpression extends VerbaExpression {
  private final IdentifierExpression identifier;
  private final GenericTypeListExpression genericParameters;
  private final QList<TupleDeclarationExpression> parameterLists = new QList<>();

  public MemberExpression(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    this.identifier = readIdentifier(lexer);
    this.genericParameters = GenericTypeListExpression.read(this, lexer);

    // Read parameterSets if they exist
    do {
      if (lexer.currentIs(EnclosureToken.class, "(")) {
        parameterLists.add(TupleDeclarationExpression.read(this, lexer));
      }
    } while (lexer.notEOF() && lexer.currentIs(EnclosureToken.class, "("));

    this.closeLexingRegion();
  }

  public IdentifierExpression readIdentifier(Lexer lexer) {
    if (lexer.currentIs(IdentifierToken.class)) {
      return IdentifierExpression.read(this, lexer);
    } else {
      return null;
    }
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

  public static MemberExpression read(VerbaExpression parent, Lexer lexer) {
    return new MemberExpression(parent, lexer);
  }

  public String memberName() {
    return this.identifier.representation();
  }

  public boolean isAnonymous() { return this.identifier == null; }

  public String representation() {
    if (genericParameters.hasItems()) {
      return String.format("%s%s",
        this.identifier.representation(),
        this.genericParameters.representation());
    } else {
      return this.identifier.representation();
    }
  }

  public boolean hasParameters() {
    return this.parameterLists.any();
  }

  public IdentifierExpression identifier() {
    return this.identifier;
  }

  public QIterable<TupleDeclarationExpression> parameterLists() {
    return this.parameterLists;
  }

  public GenericTypeListExpression genericParameterList() {
    return this.genericParameters;
  }

  public boolean hasGenericParameters() {
    return this.genericParameters.hasItems();
  }

  @Override
  public void accept(ExpressionTreeVisitor visitor) {

  }

  @Override
  public VirtualVariable accept(FunctionGraphVisitor visitor) {
    return null;
  }
}
