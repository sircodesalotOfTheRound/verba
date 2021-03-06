package com.verba.language.graph.symbols.table.tables;

import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;
import com.verba.language.platform.PlatformTypeSymbols;
import com.verba.language.graph.symbols.meta.NestedScopeMetadata;
import com.verba.language.graph.symbols.table.entries.Symbol;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.blockheader.classes.PolymorphicDeclarationExpression;
import com.verba.language.parse.expressions.categories.ExpressionSource;
import com.verba.language.parse.expressions.categories.SymbolTableExpression;
import com.verba.language.parse.expressions.statements.declaration.ValDeclarationStatement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sircodesalot on 14-5-16.
 */
public class SymbolTable {
  private static final QIterable<Symbol> EMPTY_SET = new QList<>();

  private final Scope rootTable;
  private final QList<Symbol> entries = new QList<>();
  private final Map<VerbaExpression, Symbol> entriesByInstance = new HashMap<>();
  private final Map<String, QList<Symbol>> entriesByFqn = new HashMap<>();

  public SymbolTable(SymbolTableExpression block) {
    this(new Scope(block));
  }

  public SymbolTable(Scope table) {
    this.rootTable = table;
    this.addPlatformTypes();
    this.scanTableHierarchy(table);
  }

  private void addPlatformTypes() {
    for (Symbol symbol : PlatformTypeSymbols.platformSymbols()) {
      this.putEntry(symbol);
    }
  }

  private void scanTableHierarchy(Scope table) {
    for (Symbol entry : table.entries()) {
      this.putEntry(entry);
    }

    for (Scope subTable : table.nestedTables()) {
      scanTableHierarchy(subTable);
    }
  }

  private void putEntry(Symbol entry) {
    String fqn = entry.fqn();
    VerbaExpression instance = entry.expression();

    // Add to all entry lists
    this.entries.add(entry);
    this.entriesByInstance.put(instance, entry);
    this.getEntryListByFqn(fqn).add(entry);
  }

  private QList<Symbol> getEntryListByFqn(String fqn) {
    // If there is already a list associated with this name,
    // then just return that.
    if (this.entriesByFqn.containsKey(fqn)) {
      return this.entriesByFqn.get(fqn);
    }

    // Else create a new list
    QList<Symbol> entryList = new QList<>();
    this.entriesByFqn.put(fqn, entryList);

    return entryList;
  }

  public QIterable<Symbol> entries() {
    return this.entries;
  }

  public QIterable<Symbol> findAllMatchingFqn(String fqn) {
    QIterable<Symbol> matches =  this.entriesByFqn.get(fqn);

    if (matches != null) {
      return matches;
    } else {
      return EMPTY_SET;
    }
  }

  public Symbol findSymbolForType(String fqn) {
    if (PlatformTypeSymbols.isNativeTypeSymbol(fqn)) {
      return PlatformTypeSymbols.findNativeTypeSymbolByName(fqn);
    }

    return this.findAllMatchingFqn(fqn)
      .single(entry -> entry.is(PolymorphicDeclarationExpression.class));
  }

  // TODO: Make the scope resolving functions more generally useful.
  public Scope resolveScope(VerbaExpression expression) {
    if (expression == null) {
      return null;
    } else if (this.entriesByInstance.containsKey(expression)) {
      Symbol symbol = this.entriesByInstance.get(expression);

      return symbol
        .metadata()
        .ofType(NestedScopeMetadata.class)
        .single()
        .nestedScope();
    }

    return resolveScope(expression.parent());
  }

  // TODO: Make the scope resolving functions more generally useful.
  public Scope resolveScope(ValDeclarationStatement declaration) {
    return this.entriesByInstance.get(declaration).scope();
  }

  public QIterable<ExpressionSource> sources() { return this.entries().map(Symbol::source).distinct(); }

  public Symbol findByIndex(int index) {
    return this.entries.get(index);
  }

  public Symbol findByInstance(VerbaExpression instance) {
    return this.entriesByInstance.get(instance);
  }

  public Scope rootTable() {
    return this.rootTable;
  }
}
