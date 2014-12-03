package com.verba.language.parsing.expressions.blockheader.classes;

import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;
import com.verba.language.build.event.BuildEvent;
import com.verba.language.graph.analysis.expressions.profiles.PolymorphicExpressionBuildEventHandler;
import com.verba.language.graph.analysis.expressions.tools.BuildAnalysis;
import com.verba.language.graph.symbols.table.entries.SymbolTableEntry;
import com.verba.language.graph.symbols.table.tables.GlobalSymbolTable;
import com.verba.language.graph.symbols.table.tables.ScopedSymbolTable;
import com.verba.language.graph.visitors.SyntaxGraphVisitor;
import com.verba.language.parsing.expressions.StaticSpaceExpression;
import com.verba.language.parsing.expressions.VerbaExpression;
import com.verba.language.parsing.expressions.block.BlockDeclarationExpression;
import com.verba.language.parsing.expressions.blockheader.NamedBlockExpression;
import com.verba.language.parsing.expressions.blockheader.generic.GenericTypeListExpression;
import com.verba.language.parsing.expressions.categories.*;
import com.verba.language.parsing.expressions.containers.tuple.TupleDeclarationExpression;
import com.verba.language.parsing.expressions.members.FullyQualifiedNameExpression;
import com.verba.language.parsing.expressions.members.MemberExpression;
import com.verba.language.parsing.lexing.Lexer;
import com.verba.language.parsing.tokens.identifiers.KeywordToken;
import com.verba.language.parsing.tokens.operators.mathop.OperatorToken;

/**
 * Created by sircodesalot on 14-2-17.
 */
public class PolymorphicExpression extends VerbaExpression
  implements NamedBlockExpression,
  ParameterizedExpression,
  GenericallyParameterizedExpression,
  SymbolTableExpression,
  BuildEvent.NotifySymbolTableBuildEvent
{
  private final PolymorphicExpressionBuildEventHandler buildProfile = new PolymorphicExpressionBuildEventHandler(this);
  private final FullyQualifiedNameExpression identifier;
  private BlockDeclarationExpression block;

  private QIterable<TypeDeclarationExpression> traits;

  private PolymorphicExpression(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    if (lexer.currentIs(KeywordToken.class, "class") || lexer.currentIs(KeywordToken.class, "trait")) {
      lexer.readCurrentAndAdvance(KeywordToken.class, "class");
    }

    this.identifier = FullyQualifiedNameExpression.read(this, lexer);
    this.traits = readBaseTypes(lexer);
    this.block = BlockDeclarationExpression.read(this, lexer);

    this.closeLexingRegion();
  }

  private QIterable<TypeDeclarationExpression> readBaseTypes(Lexer lexer) {
    QList<TypeDeclarationExpression> baseTypes = new QList<>();

    if (lexer.notEOF() && lexer.currentIs(OperatorToken.class, ":")) {
      lexer.readCurrentAndAdvance(OperatorToken.class, ":");
    } else {
      return baseTypes;
    }

    do {
      baseTypes.add(TypeDeclarationExpression.read(this, lexer));

      // Read a comma if that's the next item, else break.
      if (lexer.notEOF() && lexer.currentIs(OperatorToken.class, ",")) {
        lexer.readCurrentAndAdvance(OperatorToken.class, ",");
      }
      else {
        break;
      }

    } while (lexer.notEOF());

    return baseTypes;
  }

  public static PolymorphicExpression read(VerbaExpression parent, Lexer lexer) {
    return new PolymorphicExpression(parent, lexer);
  }

  // Build Events
  @Override
  public void beforeSymbolTableAssociation(BuildAnalysis analysis, StaticSpaceExpression buildAnalysis) {
    this.buildProfile.beforeSymbolTableAssociation(analysis, buildAnalysis);
  }

  @Override
  public void afterSymbolTableAssociation(BuildAnalysis buildAnalysis, StaticSpaceExpression staticSpace, GlobalSymbolTable symbolTable) {
    this.buildProfile.afterSymbolTableAssociation(buildAnalysis, staticSpace, symbolTable);
  }

  // Accessors
  public FullyQualifiedNameExpression declaration() {
    return this.identifier;
  }

  public MemberExpression primaryIdentifier() {
    return this.declaration().first();
  }

  public QIterable<TupleDeclarationExpression> inlineParameters() {
    return this.primaryIdentifier().parameterLists();
  }

  public GenericTypeListExpression genericParameters() {
    return this.primaryIdentifier().genericParameterList();
  }

  public QIterable<SymbolTableEntry> traitSymbolTableEntries() { return this.buildProfile.traitEntries(); }

  public QIterable<TypeDeclarationExpression> traits() {
    return this.traits;
  }

  public QIterable<SymbolTableEntry> allMembers() { return this.buildProfile.allMembers(); }

  public QIterable<SymbolTableEntry> immediateMembers() { return this.buildProfile.immediateMembers(); }

  public boolean isMember(String name) {
    return false;
  }

  public boolean isImmediateMember(String name) {
    return buildProfile.isImmediateMember(name);
  }

  public QIterable<SymbolTableExpression> findMembersByName(String name) {
    return null;
  }

  @Override
  public BlockDeclarationExpression block() {
    return this.block;
  }

  public boolean isInlineClass() {
    return (this.primaryIdentifier().hasParameters() || !this.hasBlock());
  }

  @Override
  public boolean hasGenericParameters() {
    return this.primaryIdentifier().hasGenericParameters();
  }

  public boolean hasTraits() {
    return (this.traits != null);
  }

  public boolean hasBlock() {
    return (this.block != null && this.block.hasItems());
  }

  @Override
  public String name() {
    return this.identifier.members().first().memberName();
  }

  @Override
  public void accept(SyntaxGraphVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public void accept(ScopedSymbolTable symbolTable) {
    symbolTable.visit(this);
  }

  @Override
  public boolean hasParameters() { return this.primaryIdentifier().hasParameters(); }

  @Override
  public QIterable<TupleDeclarationExpression> parameterSets() { return this.primaryIdentifier().parameterLists(); }


}