package com.verba.language.graph.symbols.table.entries;

import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;
import com.verba.language.graph.symbols.meta.ParameterSymbolMetadata;
import com.verba.language.graph.symbols.meta.interfaces.SymbolTableMetadata;
import com.verba.language.graph.symbols.meta.types.SystemTypeMetadata;
import com.verba.language.graph.symbols.meta.types.TypeDefinitionMetadata;
import com.verba.language.graph.symbols.meta.types.UserDefinedTypeMetadata;
import com.verba.language.graph.symbols.table.tables.Scope;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.categories.ExpressionSource;

import java.io.Serializable;

/**
 * Created by sircodesalot on 14-3-9.
 */
public class Symbol {
  private final String name;
  private final Scope table;
  private final VerbaExpression object;
  private final ExpressionSource source;
  private final String fqn;
  private final QList<SymbolTableMetadata> metadata = new QList<>();
  private SymbolInfo info;


  public Symbol(String name, Scope table, VerbaExpression object, SymbolTableMetadata... metadata) {
    this.name = name;
    this.table = table;
    this.object = object;
    this.source = discoverSource(object);
    this.fqn = determineFqn();

    for (SymbolTableMetadata item : metadata) {
      this.metadata.add(item);
    }

    this.info = new SymbolInfo(new QList<>(metadata));
  }

  public String name() {
    return this.name;
  }

  public Scope scope() {
    return this.table;
  }

  public VerbaExpression expression() {
    return this.object;
  }

  public <T> T expressionAs(Class<T> type) {
    return (T) this.object;
  }

  public Class type() {
    return this.object.getClass();
  }

  public void addMetadata(SymbolTableMetadata metadata) {
    this.metadata.add(metadata);
    this.info = new SymbolInfo(this.metadata);
  }

  public QIterable<SymbolTableMetadata> metadata() { return this.metadata; }

  public ExpressionSource source() { return this.source; }

  public boolean isParameter() { return this.info.isParameter(); }
  public boolean isType() { return this.info.isType(); }
  public boolean isSystemType() { return this.info.isSystemType(); }
  public boolean isUserDefinedType() { return this.info.isUserDefinedType(); }

  private ExpressionSource discoverSource(VerbaExpression object) {
    if (object == null) {
      return null;
    }

    if (object.is(ExpressionSource.class)) {
      return (ExpressionSource)object;
    } else {
      return discoverSource(object.parent());
    }
  }

  private String determineFqn() {
    if (!table.fqn().isEmpty()) return String.format("%s.%s", table.fqn(), name);
    else return name;
  }

  public String fqn() { return this.fqn; }

  public <T> boolean is(Class<T> type) {
    return type.isAssignableFrom(object.getClass());
  }
}
