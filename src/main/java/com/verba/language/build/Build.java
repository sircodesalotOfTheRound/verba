package com.verba.language.build;

import com.javalinq.implementations.QSet;
import com.javalinq.interfaces.QIterable;
import com.javalinq.tools.Partition;
import com.verba.language.build.event.BuildEvent;
import com.verba.language.build.event.BuildEventLauncher;
import com.verba.language.build.event.BuildEventSet;
import com.verba.language.build.source.CodeUnit;
import com.verba.language.emit.images.ObjectImageSet;
import com.verba.language.emit.images.interfaces.ObjectImage;
import com.verba.language.emit.verbatim.persist.VerbatimFileGenerator;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.parse.codestream.StringBasedCodeStream;
import com.verba.language.parse.expressions.StaticSpaceExpression;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.expressions.codepage.VerbaCodePage;
import com.verba.language.parse.lexing.VerbaMemoizingLexer;
import com.verba.tools.files.FileTools;

/**
 * Created by sircodesalot on 14/11/20.
 */
public class Build {
  private BuildEventSet buildEventSet;
  private BuildProfile buildProfile;
  private StaticSpaceExpression staticSpace;
  private SymbolTable symbolTable;
  private ObjectImageSet images;
  private QIterable<BuildEvent> eventSubscribers;
  private final BuildConfiguration configuration;
  private final VerbaCodePage page;

  public Build(BuildConfiguration configuration) {
    this.configuration = configuration;
    this.page = this.configuration.codeUnits()
      .map(unit -> VerbaCodePage.fromFile(null, unit.path()))
      .single();
  }

  public void build() {
    this.buildProfile = new BuildProfile(configuration);
    this.staticSpace = new StaticSpaceExpression(page);
    this.buildEventSet = new BuildEventSet(buildProfile, staticSpace);
    this.buildEventSet.afterParse();

    if (!this.buildProfile.shouldCreateSymbolTable()) return;

    this.buildEventSet.beforeAssociateSymbols();
    this.symbolTable = new SymbolTable(this.staticSpace);
    this.buildEventSet.afterAssociateSymbols(this.symbolTable);

    this.buildEventSet.validateBuild(this.symbolTable);

    if (!this.buildProfile.shouldEmitCode()) return;

    this.buildEventSet.beforeCodeGeneration(this.symbolTable);

    this.images = this.buildEventSet.generateObjectImages(symbolTable);
  }

  public SymbolTable symbolTable() { return this.symbolTable; }
  public QIterable<VerbaExpression> allExpressions() { return this.staticSpace.allExpressions(); }
  public Partition<Class, VerbaExpression> expressionsByType() { return this.staticSpace.expressionsByType(); }

  public ObjectImageSet images() { return this.images; }

  public boolean saveAssembly(String path) {
    VerbatimFileGenerator generator = new VerbatimFileGenerator(this.images);
    return generator.save(path);
  }

  /*
  @Deprecated
  public static Build fromString(String code, BuildConfiguration configuration) {
    StringBasedCodeStream codeStream = new StringBasedCodeStream(code);
    VerbaMemoizingLexer lexer = new VerbaMemoizingLexer("MemoryCodefile.v", codeStream, false, false);

    return new Build(VerbaCodePage.read(null, lexer), configuration);
  }


  public static Build fromSingleFile(String path) {
    return fromSingleFile(path, new BuildConfiguration());
  }

  public static Build fromSingleFile(String path, BuildConfiguration configuration) {
    String content = FileTools.readAllText(path);
    return Build.fromString(content, configuration);
  }
  */
}
