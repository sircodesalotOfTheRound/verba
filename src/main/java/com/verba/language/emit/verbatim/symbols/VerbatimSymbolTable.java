package com.verba.language.emit.verbatim.symbols;

import com.verba.language.emit.images.interfaces.ImageType;
import com.verba.language.emit.images.interfaces.ObjectImage;

/**
 * Created by sircodesalot on 14/12/5.
 */
public class VerbatimSymbolTable implements ObjectImage {
  @Override
  public String name() { return null; }

  @Override
  public ImageType imageType() { return null; }

  @Override
  public int size() { return 0; }

  @Override
  public byte[] data() { return new byte[0]; }
}
