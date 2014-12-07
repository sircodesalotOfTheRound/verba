package com.verba.language.build.event.subscriptions;

import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;
import com.verba.language.build.BuildProfile;
import com.verba.language.build.event.BuildEvent;
import com.verba.language.build.event.BuildEventSubscription;
import com.verba.language.parse.expressions.StaticSpaceExpression;
import com.verba.language.parse.expressions.codepage.VerbaCodePage;
import com.verba.language.parse.expressions.withns.WithNsExpression;

/**
 * Created by sircodesalot on 14/11/23.
 */
public class VerbaCodePageBuildEventSubscription extends BuildEventSubscription<VerbaCodePage>
  implements BuildEvent.NotifyParsingBuildEvent
{
  private QList<String> namespaces = new QList<>();

  public VerbaCodePageBuildEventSubscription(VerbaCodePage expression) {
    super(expression);
  }

  @Override
  public void afterParse(BuildProfile analysis, StaticSpaceExpression buildAnalysis) {
    QIterable<String> namespaceRepresentations = this.expression()
      .expressionsByType(WithNsExpression.class)
      .map(ns -> ns.namespace().representation());

    this.namespaces.add(namespaceRepresentations);
  }

  public QIterable<String> namespaces() { return namespaces; }

}
