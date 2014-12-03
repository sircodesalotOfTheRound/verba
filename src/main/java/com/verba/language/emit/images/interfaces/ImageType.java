package com.verba.language.emit.images.interfaces;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by sircodesalot on 14/9/26.
 */
public enum ImageType {
  FUNCTION,
  STRING_TABLE;

  public byte[] asByteArray() {
    final byte[] functionCode = new byte[] { 0x11, 0x11 };

    switch (this) {
      case FUNCTION:
        return functionCode;
    }

    throw new NotImplementedException();
  }
}