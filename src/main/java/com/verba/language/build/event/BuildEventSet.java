package com.verba.language.build.event;

import com.javalinq.implementations.QSet;
import com.javalinq.interfaces.QIterable;
import com.verba.language.build.BuildProfile;
import com.verba.language.graph.symbols.table.entries.Symbol;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.parse.expressions.StaticSpaceExpression;
import com.verba.language.parse.expressions.VerbaExpression;

/**
 * Created by sircodesalot on 14/12/10.
 */
public class BuildEventSet {
  private final QIterable<BuildEvent> eventSubscribers;
  private final BuildProfile buildProfile;
  private final StaticSpaceExpression staticSpace;

  public BuildEventSet(BuildProfile buildProfile, StaticSpaceExpression staticSpace) {
    this.buildProfile = buildProfile;
    this.staticSpace = staticSpace;
    this.eventSubscribers = this.determineEventSubscribers(staticSpace.allExpressions());
  }

  private QIterable<BuildEvent> determineEventSubscribers(QIterable<VerbaExpression> expressions) {
    QIterable<BuildEvent> expressionsSubscribingToEvents = expressions
      .ofType(BuildEvent.class);

    QIterable<BuildEvent> expressionsWithDelegatingSubscriptionObject = expressions
      .ofType(BuildEvent.ContainsEventSubscriptionObject.class)
      .map(BuildEvent.ContainsEventSubscriptionObject::buildEventObject);

    QSet<BuildEvent> allSubscribingObjects = new QSet<>();
    allSubscribingObjects.add(expressionsSubscribingToEvents);
    allSubscribingObjects.add(expressionsWithDelegatingSubscriptionObject);

    return allSubscribingObjects;
  }

  // Events
  public void afterParse() {
    BuildEventLauncher<BuildEvent.NotifyParsingBuildEvent> launcher
      = new BuildEventLauncher<>(BuildEvent.NotifyParsingBuildEvent.class, eventSubscribers);

    launcher.launchEvent(expression -> expression.afterParse(buildProfile, this.staticSpace));
  }

  public void beforeAssociateSymbols() {
    BuildEventLauncher<BuildEvent.NotifySymbolTableBuildEvent> launcher
      = new BuildEventLauncher<>(BuildEvent.NotifySymbolTableBuildEvent.class, eventSubscribers);

    launcher.launchEvent(expression -> expression.beforeSymbolsGenerated(buildProfile, this.staticSpace));
  }

  public void afterAssociateSymbols(SymbolTable symbolTable) {
    BuildEventLauncher<BuildEvent.NotifySymbolTableBuildEvent> launcher
      = new BuildEventLauncher<>(BuildEvent.NotifySymbolTableBuildEvent.class, eventSubscribers);

    launcher.launchEvent(expression -> expression.afterSymbolsGenerated(buildProfile, this.staticSpace, symbolTable));
    launcher.launchEvent(expression -> expression.onResolveSymbols(buildProfile, this.staticSpace, symbolTable));
  }

  public void validateBuild(SymbolTable symbolTable) {
    BuildEventLauncher<BuildEvent.NotifyReadyToCompileEvent> launcher
      = new BuildEventLauncher<>(BuildEvent.NotifyReadyToCompileEvent.class, eventSubscribers);

    launcher.launchEvent(expression -> expression.validateReadyToCompile(buildProfile, staticSpace, symbolTable));
  }

  public void beforeCodeGeneration(SymbolTable symbolTable) {
    BuildEventLauncher<BuildEvent.NotifyCodeGenerationEvent> launcher
      = new BuildEventLauncher<>(BuildEvent.NotifyCodeGenerationEvent.class, eventSubscribers);

    launcher.launchEvent(expression -> expression.beforeCodeGeneration(buildProfile, staticSpace, symbolTable));
  }
}
