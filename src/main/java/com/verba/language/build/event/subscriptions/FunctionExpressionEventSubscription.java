package com.verba.language.build.event.subscriptions;

import com.verba.language.build.BuildAnalysis;
import com.verba.language.build.event.BuildEvent;
import com.verba.language.build.event.ExpressionBuildEventSubscription;
import com.verba.language.emit.images.interfaces.ObjectImage;
import com.verba.language.emit.images.types.specialized.FunctionObjectImage;
import com.verba.language.graph.symbols.resolution.FunctionReturnTypeResolver;
import com.verba.language.graph.symbols.table.entries.SymbolTableEntry;
import com.verba.language.graph.symbols.table.tables.GlobalSymbolTable;
import com.verba.language.parse.expressions.StaticSpaceExpression;
import com.verba.language.parse.expressions.blockheader.functions.FunctionDeclarationExpression;

/**
 * Created by sircodesalot on 14/11/23.
 */
public class FunctionExpressionEventSubscription extends ExpressionBuildEventSubscription<FunctionDeclarationExpression>
  implements BuildEvent.NotifySymbolTableBuildEvent, BuildEvent.NotifyObjectEmitEvent
{
  private SymbolTableEntry returnType;
  public FunctionExpressionEventSubscription(FunctionDeclarationExpression expression) {
    super(expression);
  }

  @Override
  public void beforeSymbolTableAssociation(BuildAnalysis analysis, StaticSpaceExpression buildAnalysis) {

  }

  @Override
  public void afterSymbolTableAssociation(BuildAnalysis buildAnalysis, StaticSpaceExpression staticSpace, GlobalSymbolTable symbolTable) {
    this.returnType = this.determineReturnType(symbolTable);
  }

  private SymbolTableEntry determineReturnType(GlobalSymbolTable symbolTable) {
    FunctionReturnTypeResolver returnTypeResolver = new FunctionReturnTypeResolver(symbolTable, this.expression());
    return returnTypeResolver.resolve();
  }

  @Override
  public ObjectImage onGenerateObjectImage(BuildAnalysis buildAnalysis, StaticSpaceExpression staticSpace, GlobalSymbolTable symbolTable) {
    FunctionObjectImage image = new FunctionObjectImage(this.expression(), staticSpace, symbolTable);

    if (buildAnalysis.isDebugBuild()) {
      image.displayCoreDump();
    }

    return image;
  }

  public SymbolTableEntry returnType() { return this.returnType; }
}