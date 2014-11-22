package com.verba.language.graph.symbols.resolution.interfaces;

/**
 * Created by sircodesalot on 14-5-16.
 */
public interface SymbolResolver<T, U extends SymbolResolutionInfo> {
  U resolve(T expression);
}