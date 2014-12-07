package com.verba.language.build.event.subscriptions;

import com.verba.language.build.BuildProfile;
import com.verba.language.build.event.BuildEvent;
import com.verba.language.build.event.BuildEventSubscription;
import com.verba.language.graph.expressions.retval.ReturnStatementTypeResolver;
import com.verba.language.graph.symbols.table.entries.Symbol;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.parse.expressions.StaticSpaceExpression;
import com.verba.language.parse.expressions.statements.returns.ReturnStatementExpression;

/**
 * Created by sircodesalot on 14/12/6.
 */
public class ReturnStatementEventSubscription extends BuildEventSubscription<ReturnStatementExpression>
  implements BuildEvent.NotifySymbolTableBuildEvent
{
  private SymbolTable symbolTable;
  private ReturnStatementTypeResolver typeResolver;

  public ReturnStatementEventSubscription(ReturnStatementExpression statement) {
    super(statement);
  }

  @Override
  public void beforeSymbolsGenerated(BuildProfile analysis, StaticSpaceExpression buildAnalysis) {

  }

  @Override
  public void afterSymbolsGenerated(BuildProfile buildProfile, StaticSpaceExpression staticSpace, SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
    this.typeResolver = new ReturnStatementTypeResolver(this.expression(), symbolTable);
  }

  @Override
  public void onResolveSymbols(BuildProfile profile, StaticSpaceExpression staticSpace, SymbolTable symbolTable) {

  }

  public Symbol returnType() {
    return this.typeResolver.returnType();
  }

}
