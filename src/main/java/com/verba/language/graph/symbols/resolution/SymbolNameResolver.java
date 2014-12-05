package com.verba.language.graph.symbols.resolution;

import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;
import com.verba.language.graph.symbols.table.entries.Symbol;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.graph.symbols.table.tables.Scope;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.codepage.VerbaCodePage;

/**
 * Created by sircodesalot on 14/11/24.
 */
public class SymbolNameResolver {
  private final SymbolTable symbolTable;
  private final Scope scope;
  private final VerbaCodePage page;
  private final QIterable<String> namespacesInScope;

  public SymbolNameResolver(SymbolTable symbolTable, Scope scope) {
    this.symbolTable = symbolTable;
    this.scope = scope;
    this.page = discoverPage(scope);
    this.namespacesInScope = namespacesInScope(page, scope);
  }

  public QIterable<SymbolResolutionMatch> findSymbolsInScope(String name) {
    QList<SymbolResolutionMatch> matchingEntries = new QList<>();

    String fqn;
    for (String namespace : namespacesInScope) {
      if (!namespace.isEmpty()) {
        fqn = String.format("%s.%s", namespace, name);
      } else {
        fqn = name;
      }

      for (Symbol entry : symbolTable.findAllMatchingFqn(fqn)) {
        SymbolResolutionMatch match = new SymbolResolutionMatch(entry);
        matchingEntries.add(match);
      }
    }

    return matchingEntries;
  }

  private VerbaCodePage discoverPage(Scope scope) {
    if (scope.entries().any()) {
      return (VerbaCodePage) scope.entries().first().source();
    } else {
      return discoverPage((VerbaCodePage)scope.header());
    }
  }

  private VerbaCodePage discoverPage(VerbaExpression expression) {
    if (expression instanceof VerbaCodePage) {
      return (VerbaCodePage) expression;
    } else {
      return discoverPage(expression.parent());
    }
  }

  private QIterable<String> namespacesInScope(VerbaCodePage page, Scope scope) {
    QList<String> namespaces = new QList<>();

    namespaces.add(scope.fqnList());
    namespaces.add(page.importedNamespaces());

    return namespaces;
  }
}
