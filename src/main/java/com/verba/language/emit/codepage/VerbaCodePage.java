package com.verba.language.emit.codepage;

import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;
import com.javalinq.tools.Partition;
import com.verba.language.graph.analysis.expressions.analyzers.VerbaCodePageBuildProfile;
import com.verba.language.graph.analysis.expressions.tools.BuildProfileBase;
import com.verba.language.graph.visitors.SyntaxGraphVisitor;
import com.verba.language.parsing.expressions.VerbaExpression;
import com.verba.language.parsing.expressions.categories.SymbolTableExpression;
import com.verba.language.parsing.lexing.Lexer;
import com.verba.language.parsing.lexing.VerbaMemoizingLexer;
import com.verba.language.parsing.codestream.CodeStream;
import com.verba.language.parsing.codestream.FileBasedCodeStream;
import com.verba.language.graph.symbols.table.tables.ScopedSymbolTable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.InputStream;

/**
 * A Codepage is a page of Verba Code.
 */
public class VerbaCodePage extends VerbaExpression implements SymbolTableExpression {
  private VerbaCodePageBuildProfile buildProfile = new VerbaCodePageBuildProfile(this);
  private QList<VerbaExpression> expressions;
  private Partition<Class, VerbaExpression> expressionsByType;
  private String path;

  private VerbaCodePage(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);

    this.path = lexer.filename();
    this.expressions = this.readExpressions(lexer);
    this.expressionsByType = this.expressions.parition(Object::getClass);
  }

  private QList<VerbaExpression> readExpressions(Lexer lexer) {
    QList<VerbaExpression> expressionList = new QList<>();

    while (lexer.notEOF()) {
      VerbaExpression expression = VerbaExpression.read(this, lexer);
      expressionList.add(expression);
    }

    return expressionList;
  }

  public static VerbaCodePage read(VerbaExpression parent, Lexer lexer) {
    return new VerbaCodePage(parent, lexer);
  }

  @Override
  public BuildProfileBase buildProfile() { return buildProfile; }

  public QIterable<VerbaExpression> expressions() {
    return this.expressions;
  }

  public <T> QIterable<T> expressionsByType(Class<T> type) {
    if (this.expressionsByType.containsKey(type)) {
      return this.expressionsByType.get(type).cast(type);
    } else {
      return new QList<>();
    }
  }

  public String path() {
    return this.path;
  }

  // Build from a file path.
  public static VerbaCodePage fromFile(VerbaExpression parent, String path) {
    CodeStream codeStream = new FileBasedCodeStream(path);
    Lexer lexer = new VerbaMemoizingLexer(path, codeStream);

    return new VerbaCodePage(parent, lexer);
  }

  // Build from an item in a package.
  public static VerbaCodePage fromResourceStream(String path) {
    try {
      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
      Class packageClass = Class.forName(stackTrace[stackTrace.length - 1].getClassName());

      InputStream stream = packageClass.getResourceAsStream(path);
      CodeStream codeStream = new FileBasedCodeStream(path, stream);
      Lexer lexer = new VerbaMemoizingLexer(path, codeStream);

      return new VerbaCodePage(null, lexer);
    } catch (Exception ex) {
      throw new NotImplementedException();
    }
  }

  @Override
  public void accept(SyntaxGraphVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public void accept(ScopedSymbolTable symbolTable) {
    symbolTable.visit(this);
  }
}