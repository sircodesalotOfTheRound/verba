package com.verba.language.parse.expressions;

import com.javalinq.interfaces.QIterable;
import com.verba.language.build.configuration.Build;
import com.verba.language.emit.variables.VirtualVariable;
import com.verba.language.graph.expressions.functions.FunctionGraphVisitor;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.graph.visitors.ExpressionTreeNode;
import com.verba.language.graph.visitors.FunctionGraphNode;
import com.verba.language.parse.expressions.backtracking.BacktrackRuleSet;
import com.verba.language.parse.expressions.backtracking.rules.*;
import com.verba.language.parse.info.LexInfo;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.violations.ValidationError;
import com.verba.language.parse.violations.ValidationViolation;
import com.verba.language.parse.violations.ValidationViolationList;
import com.verba.language.parse.violations.ValidationWarning;
import com.verba.language.platform.expressions.PlatformExpressionBacktrackRule;

/**
 * Created by sircodesalot on 14-2-19.
 */
public abstract class VerbaExpression implements ExpressionTreeNode, FunctionGraphNode {
  private static BacktrackRuleSet<VerbaExpression> rules
    = new BacktrackRuleSet<VerbaExpression>()
    .addRule(new InfixExpressionBacktrackRule())
    .addRule(new LiteralExpressionRule())
    .addRule(new FunctionDeclarationBacktrackRule())
    .addRule(new NamespaceDeclarationBacktrackRule())
    .addRule(new PolymorphicExpressionBacktrackRule())
    .addRule(new ValDeclarationBacktrackRule())
    .addRule(new IfStatementBacktrackRule())
    .addRule(new WithNsExpressionBacktrackRule())
    .addRule(new MarkupDeclarationExpressionBacktrackRule())
    .addRule(new NamedValueExpressionBacktrackRule())
    .addRule(new GrabExpressionBacktrackRule())
    .addRule(new ImmediateFunctionExpressionBacktrackRule())
    .addRule(new ForStatementBacktrackRule())
    .addRule(new PlatformExpressionBacktrackRule())
    .addRule(new WhileStatementBacktrackRule())
    .addRule(new AssignmentStatementBacktrackRule())
    .addRule(new InjectedClassDeclarationBacktrackRule())
    .addRule(new MetaDeclarationBacktrackRule())
    .addRule(new ExtendDeclarationBacktrackRule())
    .addRule(new SignatureDeclarationBacktrackRule())
    .addRule(new HashtagDeclarationBacktrackRule())
    .addRule(new AspectDeclarationBacktrackRule())
    .addRule(new ReturnStatementRule())
    .addRule(new DeclarationModifierBacktrackRule())
    .addRule(new BooleanExpressionBacktrackRule())
    .addRule(new AsmExpressionBacktrackRule())
    .addRule(new CatchAllBacktrackRule());

  private VerbaExpression parent;

  private final Lexer lexer;
  private final LexInfo startingLexPoint;
  private LexInfo endingLexPoint;
  private final ValidationViolationList violations = new ValidationViolationList();

  public VerbaExpression(VerbaExpression parent, Lexer lexer) {
    this.parent = parent;
    this.lexer = lexer;
    this.startingLexPoint = (lexer != null && lexer.notEOF()) ? lexer.current() : null;
  }

  // Parsing
  public static VerbaExpression read(VerbaExpression parent, Lexer lexer) { return rules.resolve(parent, lexer); }

  // Lex Info
  public LexInfo startingLexPoint() {
    return this.startingLexPoint;
  }

  protected void closeLexingRegion() {
    this.endingLexPoint = this.lexer.peekPrevious();
  }

  public LexInfo endingLexPoint() {
    return this.endingLexPoint;
  }

  // Text information
  public String text() {
    int startIndex = startingAbsolutePosition();
    int endIndex = endingAbsolutePosition();

    return lexer.text().substring(startIndex, endIndex);
  }

  public int startingLine() {
    return this.startingLexPoint.line();
  }
  public int startingColumn() {
    return this.startingLexPoint.column();
  }
  public int startingAbsolutePosition() {
    return this.startingLexPoint.absolutePosition();
  }

  public int endingLine() { return this.endingLexPoint.line(); }
  public int endingColumn() { return this.endingLexPoint.column() + this.endingLexPoint.length(); }
  public int endingAbsolutePosition() { return this.endingLexPoint.absolutePosition() + this.endingLexPoint.length(); }

  // Reflection
  public <T> T as(Class<T> type) { return (T)this; }
  public <T> boolean is(Class<T> type) { return type.isAssignableFrom(this.getClass()); }

  public boolean hasParent() { return this.parent != null; }
  public VerbaExpression parent() { return this.parent; }
  public <T> boolean parentIs(Class<T> type) { return this.hasParent() && type.isAssignableFrom(this.parent.getClass()); }

  protected abstract void onChildRemoved(VerbaExpression child);

  public void setParent(VerbaExpression parent) {
    VerbaExpression previousParent = this.parent;
    this.parent = parent;

    // Notify parent when child is removed.
    if (previousParent != null) {
      previousParent.onChildRemoved(this);
    }
  }

  // Build Events
  public abstract void afterContentsParsed(Build build);
  public abstract void afterSymbolsGenerated(Build build, SymbolTable table);
  public abstract void onResolveSymbols(Build build, SymbolTable table);
  public abstract void onValidate(Build build, SymbolTable table);

  // Violations
  public QIterable<ValidationViolation> violations() { return this.violations; }

  public void addViolation(ValidationViolation violation) {
    this.violations.add(violation);
  }

  protected void addWarning(String format, Object ... args)  {
    this.addViolation(new ValidationWarning(this, format, args));
  }

  protected void addError(String format, Object ... args) {
    this.addViolation(new ValidationError(this, format, args));
  }

}
