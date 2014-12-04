package com.verba.language.emit.opcodes;

import com.verba.language.emit.images.interfaces.ObjectImageOutputStream;
import com.verba.language.emit.variables.VirtualVariable;

/**
 * Created by sircodesalot on 14/9/19.
 */
public abstract class VerbatimOpCodeBase {
  private final int opcodeNumber;
  private final String opcodeName;

  protected VerbatimOpCodeBase(int opcodeNumber, String opcodeName) {
    this.opcodeNumber = opcodeNumber;
    this.opcodeName = opcodeName;
  }

  public int opcodeNumber() { return opcodeNumber;}
  public String opcodeName() { return opcodeName; }

  public abstract void render(ObjectImageOutputStream renderer);

  // Static Op-codes
  private static VerbatimOpCodeBase endFunctionOpCode = new EndFunctionOpCode();
  private static VerbatimOpCodeBase returnOpCode = new RetOpCode();

  // Factory methods
  public static VerbatimOpCodeBase box(VirtualVariable source, VirtualVariable destination) {
    return new BoxOpCode(source, destination);
  }

  public static VerbatimOpCodeBase call(String functionName) {
    return new CallOpCode(functionName);
  }

  public static VerbatimOpCodeBase call(String functionName, Iterable<VirtualVariable> variables) {
    return new CallOpCode(functionName, variables);
  }

  public static VerbatimOpCodeBase endFunction() {
    return endFunctionOpCode;
  }

  public static VerbatimOpCodeBase ret() {
    return returnOpCode;
  }

  public static VerbatimOpCodeBase loadString(VirtualVariable variable, String text) {
    return new LdStrOpCode(variable, text);
  }

  public static VerbatimOpCodeBase stageArg(VirtualVariable variable) {
    return new StageArgOpCode(variable);
  }

  public static VerbatimOpCodeBase loaduint64(VirtualVariable variable, long value) {
    return new LdUint64OpCode(variable, value);
  }
}