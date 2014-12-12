package com.verba.language.build.event.subscriptions;

import com.verba.language.build.BuildProfile;
import com.verba.language.build.event.BuildEvent;
import com.verba.language.build.event.BuildEventSubscription;
import com.verba.language.graph.symbols.resolution.ValDeclarationTypeResolver;
import com.verba.language.graph.symbols.table.entries.Symbol;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.parse.expressions.StaticSpaceExpression;
import com.verba.language.parse.expressions.statements.declaration.ValDeclarationStatement;

/**
 * Created by sircodesalot on 14/12/7.
 */
public class ValDeclarationEventSubscription extends BuildEventSubscription<ValDeclarationStatement>
  implements BuildEvent.NotifySymbolTableBuildEvent
{
  private ValDeclarationTypeResolver typeResolver;

  public ValDeclarationEventSubscription(ValDeclarationStatement expression) {
    super(expression);
  }

  @Override
  public void beforeSymbolsGenerated(BuildProfile profile, StaticSpaceExpression staticSpace) {

  }

  @Override
  public void afterSymbolsGenerated(BuildProfile profile, StaticSpaceExpression staticSpace, SymbolTable symbolTable) {
    this.typeResolver = new ValDeclarationTypeResolver(this.expression(), symbolTable);
  }

  @Override
  public void onResolveSymbols(BuildProfile profile, StaticSpaceExpression staticSpace, SymbolTable symbolTable) {

  }

  public Symbol resolvedType() { return typeResolver.resolvedType(); }
}