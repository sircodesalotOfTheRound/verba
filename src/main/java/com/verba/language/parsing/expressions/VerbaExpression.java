package com.verba.language.parsing.expressions;

import com.javalinq.interfaces.QIterable;
import com.verba.language.graph.validation.violations.ValidationError;
import com.verba.language.graph.validation.violations.ValidationViolation;
import com.verba.language.graph.validation.violations.ValidationViolationList;
import com.verba.language.graph.validation.violations.ValidationWarning;
import com.verba.language.graph.visitors.SyntaxGraphVisitable;
import com.verba.language.parsing.expressions.backtracking.BacktrackRuleSet;
import com.verba.language.parsing.expressions.backtracking.rules.*;
import com.verba.language.parsing.Lexer;
import com.verba.language.parsing.info.LexInfo;

import java.io.Serializable;

/**
 * Created by sircodesalot on 14-2-19.
 */
public abstract class VerbaExpression implements Serializable, SyntaxGraphVisitable {
  private static BacktrackRuleSet<VerbaExpression> rules
    = new BacktrackRuleSet<VerbaExpression>()
    .addRule(new MathExpressionBacktrackRule())
    .addRule(new LiteralExpressionRule())
    .addRule(new FunctionDeclarationBacktrackRule())
    .addRule(new TaskDeclarationBacktrackRule())
    .addRule(new NamespaceDeclarationBacktrackRule())
    .addRule(new ClassDeclarationBacktrackRule())
    .addRule(new ValDeclarationBacktrackRule())
    .addRule(new IfStatementBacktrackRule())
    .addRule(new NamedValueExpressionBacktrackRule())
    .addRule(new GrabExpressionBacktrackRule())
    .addRule(new ForStatementBacktrackRule())
    .addRule(new WhileStatementBacktrackRule())
    .addRule(new AssignmentStatementBacktrackRule())
    .addRule(new InjectedClassDeclarationBacktrackRule())
    .addRule(new MetaDeclarationBacktrackRule())
    .addRule(new TraitDeclarationBacktrackRule())
    .addRule(new ExtendDeclarationBacktrackRule())
    .addRule(new SignatureDeclarationBacktrackRule())
    .addRule(new HashtagDeclarationBacktrackRule())
    .addRule(new AspectDeclarationBacktrackRule())
    .addRule(new ReturnStatementRule())
    .addRule(new CatchAllBacktrackRule());

  private VerbaExpression parent;

  private transient final Lexer lexer;
  private final LexInfo startingLexPoint;
  private LexInfo endingLexPoint;
  private ValidationViolationList violations;

  public VerbaExpression(VerbaExpression parent, Lexer lexer) {
    this.parent = parent;
    this.lexer = lexer;
    this.startingLexPoint = (lexer != null && lexer.notEOF()) ? lexer.current() : null;
  }

  // Parent
  public VerbaExpression parent() {
    return this.parent;
  }

  public void setParent(VerbaExpression parent) {
    this.parent = parent;
  }

  public boolean hasParent() {
    return this.parent != null;
  }

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

  // Testing
  public <T> boolean is(Class<T> type) {
    return type.isAssignableFrom(this.getClass());
  }

  public <T> boolean parentIs(Class<T> type) {
    return this.hasParent() && type.isAssignableFrom(this.parent.getClass());
  }

  public static VerbaExpression read(VerbaExpression parent, Lexer lexer) {
    return rules.resolve(parent, lexer);
  }

  public void addErrorViolation(VerbaExpression expression, String format, Object... args) {
    this.violations.addError(expression, format, args);
  }

  public void addWarningViolation(VerbaExpression expression, String format, Object... args) {
    this.violations.addWarning(expression, format, args);
  }

  public QIterable<ValidationViolation> violations() { return this.violations; }
  public QIterable<ValidationError> errors() { return this.violations.ofType(ValidationError.class); }
  public QIterable<ValidationWarning> warnings() { return this.violations.ofType(ValidationWarning.class); }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof VerbaExpression)) {
      return false;
    }

    if (this == obj) {
      return true;
    }

    VerbaExpression rhs = (VerbaExpression)obj;
    return startingAbsolutePosition() == rhs.startingAbsolutePosition()
      && endingAbsolutePosition() == rhs.endingAbsolutePosition();
  }
}