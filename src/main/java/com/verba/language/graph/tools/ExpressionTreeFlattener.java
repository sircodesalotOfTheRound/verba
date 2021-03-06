package com.verba.language.graph.tools;

import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;
import com.verba.language.graph.visitors.ExpressionTreeVisitor;
import com.verba.language.parse.expressions.LitFileRootExpression;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.block.BlockDeclarationExpression;
import com.verba.language.parse.expressions.blockheader.classes.PolymorphicDeclarationExpression;
import com.verba.language.parse.expressions.blockheader.functions.FunctionDeclarationExpression;
import com.verba.language.parse.expressions.blockheader.varname.NamedValueExpression;
import com.verba.language.parse.expressions.codepage.VerbaSourceCodeFile;
import com.verba.language.parse.expressions.containers.array.ArrayDeclarationExpression;
import com.verba.language.parse.expressions.containers.json.JsonExpression;
import com.verba.language.parse.expressions.containers.markup.MarkupDeclarationExpression;
import com.verba.language.parse.expressions.containers.tuple.TupleDeclarationExpression;
import com.verba.language.parse.expressions.modifiers.DeclarationModifierExrpression;
import com.verba.language.parse.expressions.rvalue.newexpression.NewExpression;
import com.verba.language.parse.expressions.rvalue.simple.BooleanExpression;
import com.verba.language.parse.expressions.rvalue.simple.NumericExpression;
import com.verba.language.parse.expressions.rvalue.simple.UtfExpression;
import com.verba.language.parse.expressions.statements.assignment.AssignmentStatementExpression;
import com.verba.language.parse.expressions.statements.declaration.ValDeclarationStatement;
import com.verba.language.parse.expressions.statements.returns.ReturnStatementExpression;
import com.verba.language.parse.expressions.withns.WithNsExpression;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Flattens a tree of expressions into a QList.
 */
public class ExpressionTreeFlattener extends ExpressionTreeVisitor implements Serializable, QIterable<VerbaExpression> {
  private final QList<VerbaExpression> expressions = new QList<>();

  public ExpressionTreeFlattener(VerbaExpression root) {
    root.accept(this);
  }

  public QIterable<VerbaExpression> expressions() {
    return this.expressions;
  }

  @Override
  public Iterator<VerbaExpression> iterator() {
    return expressions.iterator();
  }

  private void add(VerbaExpression expression) {
    this.expressions.add(expression);
  }

  public void visit(LitFileRootExpression node) {
    add(node);
    this.visitAll(node.pages().cast(VerbaExpression.class));
  }

  public void visit(NamedValueExpression node) {
    add(node);

    if (node.hasTypeConstraint()) {
      VerbaExpression expression = (VerbaExpression)node.typeConstraint();
      expression.accept(this);
    }
  }

  // Todo: Should this also read statements as well? Probabaly?

  public void visit(PolymorphicDeclarationExpression classDeclaration) {
    add(classDeclaration);

    this.visitAll(classDeclaration.block());
  }

  public void visit(FunctionDeclarationExpression function) {
    add(function);

    this.visitAll(function.parameterSets()
      .flatten(TupleDeclarationExpression::items)
      .cast(VerbaExpression.class));
    this.visitAll(function.block());
  }

  public void visit(ArrayDeclarationExpression array) {
    add(array);
    this.visitAll(array.items());
  }

  public void visit(JsonExpression jsonExpression) {
    add(jsonExpression);
    visitAll(jsonExpression.items().cast(VerbaExpression.class));
  }

  public void visit(TupleDeclarationExpression tuple) {
    add(tuple);
    this.visitAll(tuple.items().cast(VerbaExpression.class));
  }

  public void visit(BlockDeclarationExpression block) {
    add(block);
    this.visitAll(block);
  }

  public void visit(VerbaSourceCodeFile page) {
    add(page);
    this.visitAll(page.allExpressions());
  }

  @Override
  public void visit(ReturnStatementExpression returnStatementExpression) {
    add(returnStatementExpression);
  }

  @Override
  public void visit(UtfExpression utfExpression) {
   throw new NotImplementedException();
  }

  @Override
  public void visit(AssignmentStatementExpression assignmentStatementExpression) {
    throw new NotImplementedException();
  }

  @Override
  public void visit(NumericExpression expression) {

  }

  @Override
  public void visit(ValDeclarationStatement valDeclarationStatement) {
    add(valDeclarationStatement);
  }

  @Override
  public void visit(WithNsExpression withNsExpression) {

  }

  @Override
  public void visit(MarkupDeclarationExpression markupDeclarationExpression) {
    add(markupDeclarationExpression);
  }

  @Override
  public void visit(DeclarationModifierExrpression declarationModifierExrpression) {
    declarationModifierExrpression.modifiedExpression().accept(this);
  }

  @Override
  public void visit(NewExpression newExpression) {

  }

  @Override
  public void visit(BooleanExpression expression) {

  }

}
