package com.verba.language.parse.expressions;

import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;
import com.javalinq.tools.Partition;
import com.verba.language.build.event.BuildEvent;
import com.verba.language.build.event.BuildEventSubscriptionBase;
import com.verba.language.build.event.subscriptions.StaticSpaceBuildEventSubscription;
import com.verba.language.graph.symbols.table.tables.Scope;
import com.verba.language.graph.visitors.SyntaxGraphVisitor;
import com.verba.language.parse.expressions.categories.SymbolTableExpression;
import com.verba.language.parse.expressions.codepage.VerbaCodePage;

/**
 * Created by sircodesalot on 14-5-14.
 */
public class StaticSpaceExpression extends VerbaExpression implements SymbolTableExpression, BuildEvent.ContainsEventSubscriptionObject {
  private final StaticSpaceBuildEventSubscription buildProfile = new StaticSpaceBuildEventSubscription(this);
  private final QList<VerbaExpression> allExpressions;
  private final Partition<Class, VerbaExpression> expressionsByType;
  private final QList<VerbaCodePage> pages;

  public StaticSpaceExpression(Iterable<VerbaCodePage> pages) {
    super(null, null);

    this.pages = new QList<>(pages);
    this.allExpressions = extractExpressionsFromPages(this.pages);
    this.expressionsByType = partitionExpressions(allExpressions);
  }

  public StaticSpaceExpression(VerbaCodePage ... pages) {
    super(null, null);

    this.pages = new QList<>(pages);
    this.allExpressions = extractExpressionsFromPages(this.pages);
    this.expressionsByType = partitionExpressions(allExpressions);
  }

  private Partition<Class, VerbaExpression> partitionExpressions(QIterable<VerbaExpression> expressions) {
    return this.allExpressions.parition(Object::getClass);
  }

  private QList<VerbaExpression> extractExpressionsFromPages(QIterable<VerbaCodePage> pages) {
    QList<VerbaExpression> allExpressionsFromPage = new QList<>(pages.cast(VerbaExpression.class));
    allExpressionsFromPage.add(pages.flatten(VerbaCodePage::allExpressions));

    return allExpressionsFromPage;
  }

  public boolean containsExpressionsOfType(Class type) {
    return this.expressionsByType.containsKey(type);
  }

  public QIterable<VerbaCodePage> pages() { return this.pages; }

  public QIterable<VerbaExpression> allExpressions() { return this.allExpressions; }

  public Partition<Class, VerbaExpression> expressionsByType() { return this.expressionsByType; }

  @Override
  public void accept(SyntaxGraphVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public void accept(Scope symbolTable) { symbolTable.visit(this); }

  @Override
  public BuildEventSubscriptionBase buildEventObject() { return buildProfile; }
}
