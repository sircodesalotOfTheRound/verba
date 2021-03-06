package com.verba.language.emit.opcodes;

import com.verba.language.emit.images.interfaces.ObjectImageOutputStream;
import com.verba.language.emit.opcodes.binary.VerbatimOpCodeBinaryValue;
import com.verba.language.emit.variables.VirtualVariable;

/**
 * Created by sircodesalot on 14/9/23.
 */
public class BoxOpCode extends VerbatimOpCodeBase {
  private static final VerbatimOpCodeBinaryValue BOX = VerbatimOpCodeBinaryValue.BOX;

  private final VirtualVariable source;
  private final VirtualVariable destination;

  public BoxOpCode(VirtualVariable source, VirtualVariable destination) {
    super(BOX);

    this.source = source;
    this.destination = destination;
  }

  @Override
  public void render(ObjectImageOutputStream renderer) {
    renderer.writeInt8("destination", destination.variableNumber());
    renderer.writeInt8("source", source.variableNumber());
  }
}
