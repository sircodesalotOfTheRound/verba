package com.verba.language.graph.symbols.resolution.metatags;

import com.verba.language.parsing.expressions.VerbaExpression;
import com.verba.language.parsing.expressions.blockheader.classes.ClassDeclarationExpression;
import com.verba.language.parsing.expressions.blockheader.functions.FunctionDeclarationExpression;
import com.verba.language.parsing.expressions.blockheader.functions.TaskDeclarationExpression;
import com.verba.language.parsing.expressions.categories.MetaTagExpression;
import com.verba.language.parsing.expressions.tags.hashtag.HashTagExpression;
import com.verba.language.graph.symbols.resolution.interfaces.SymbolResolver;
import com.verba.language.graph.symbols.table.entries.SymbolTableEntry;
import com.verba.language.graph.symbols.table.tables.GlobalSymbolTable;
import com.verba.language.graph.symbols.table.tables.ScopedSymbolTable;

/**
 * Created by sircodesalot on 14-5-13.
 */
public class HashtagResolver implements SymbolResolver<HashTagExpression, MetaTagResolutionInfo> {
  public final GlobalSymbolTable table;

  public HashtagResolver(GlobalSymbolTable table) {
    this.table = table;
  }

  @Override
  public MetaTagResolutionInfo resolve(HashTagExpression expression) {
    SymbolTableEntry entry = this.table.getByInstance(expression);
    SymbolTableEntry bindingTarget = findBindingTarget(entry.table(), entry);

    return new MetaTagResolutionInfo(bindingTarget);
  }

  private SymbolTableEntry findBindingTarget(ScopedSymbolTable table, SymbolTableEntry entry) {
    int index = table.getIndex(entry);

    // Bind the hashtag to the next function
    while (index++ < table.entries().count()) {
      SymbolTableEntry possiblyBindableEntry = table.get(index);
      VerbaExpression expression = possiblyBindableEntry.instance();

      // The next expression must be another MetaTag
      // or the binding target (Function, Class, Task).
      // Otherwise the formation isn't legal.
      if (expression instanceof MetaTagExpression)
        continue;

      else if (expression instanceof FunctionDeclarationExpression
        || expression instanceof ClassDeclarationExpression
        || expression instanceof TaskDeclarationExpression) {

        return possiblyBindableEntry;

      } else break;
    }

    return null;
  }
}