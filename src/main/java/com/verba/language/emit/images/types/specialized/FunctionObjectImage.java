package com.verba.language.emit.images.types.specialized;

import com.verba.language.build.configuration.Build;
import com.verba.language.build.targets.artifacts.StringTableArtifact;
import com.verba.language.emit.header.stringtable.StringTableStringEntry;
import com.verba.language.emit.images.interfaces.AppendableObjectImage;
import com.verba.language.emit.images.interfaces.ImageType;
import com.verba.language.emit.images.interfaces.ObjectImage;
import com.verba.language.emit.images.types.common.InMemoryObjectImage;
import com.verba.language.emit.opcodes.VerbatimOpCodeBase;
import com.verba.language.graph.expressions.functions.FunctionGraphVisitor;
import com.verba.language.graph.symbols.table.tables.SymbolTable;
import com.verba.language.parse.expressions.blockheader.functions.FunctionDeclarationExpression;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * This facade makes it easier to write an object image.
 */
public class FunctionObjectImage implements ObjectImage {
  private final FunctionGraphVisitor functionGraphVisitor;
  private final AppendableObjectImage objectImage;
  private final StringTableStringEntry imageName;
  private final StringTableArtifact stringTable;
  private boolean isFrozen = false;

  public FunctionObjectImage(FunctionDeclarationExpression declaration,
                             Build build,
                             SymbolTable symbolTable,
                             StringTableArtifact stringTable) {

    this.functionGraphVisitor = new FunctionGraphVisitor(build, declaration, symbolTable);
    this.stringTable = stringTable;
    this.imageName = stringTable.addString(declaration.name());
    this.objectImage = new InMemoryObjectImage(declaration.name(), ImageType.FUNCTION);
  }

  private void generateOpCodeList() {
    objectImage.writeString("name", this.imageName);
    objectImage.writeInt32("image-length", (int) functionGraphVisitor.opcodes().count());

    for (VerbatimOpCodeBase opCode : functionGraphVisitor.opcodes()) {
      objectImage.writeOpCode(opCode);
      opCode.render(objectImage);
    }
  }

  public void displayCoreDump() {
    byte[] byteData = this.data();
    System.out.println(String.format("Image: %s (%s bytes)", objectImage.name(), objectImage.data().length));

    int count = 0;
    for (byte data : byteData) {
      if (count++ > 0 && count % 10 == 0) {
        System.out.println();
      }

      System.out.print(String.format("%02x ", data));
    }

    System.out.println();
  }

  @Override
  public String name() { return objectImage.name(); }

  @Override
  public ImageType imageType() { return objectImage.imageType(); }

  @Override
  public long size() { return objectImage.size(); }

  @Override
  public byte[] data() {
    if (!isFrozen) {
      this.generateOpCodeList();
      this.isFrozen = true;
    }

    return objectImage.data();
  }
}
