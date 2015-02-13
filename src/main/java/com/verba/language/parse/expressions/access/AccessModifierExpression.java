package com.verba.language.parse.expressions.access;

import com.verba.language.graph.visitors.ExpressionTreeVisitor;
import com.verba.language.parse.expressions.VerbaExpression;
import com.verba.language.parse.info.LexInfo;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.identifiers.KeywordToken;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Created by sircodesalot on 14-5-20.
 */
public class AccessModifierExpression extends VerbaExpression {
  public static final Set<String> ACCESS_MODIFIERS = new Supplier<Set<String>>() {
    @Override
    public Set<String> get() {
      Set<String> modifiers = new HashSet<>();
      modifiers.add("public");
      modifiers.add("protected");
      modifiers.add("private");
      modifiers.add("internal");
      modifiers.add("abstract");

      return modifiers;
    }
  }.get();

  private LexInfo accessModifier;

  public AccessModifierExpression(VerbaExpression parent, Lexer lexer) {
    super(parent, lexer);


  }

  @Override
  protected void onChildRemoved(VerbaExpression child) {

  }

  @Override
  public void onParse(VerbaExpression parent, Lexer lexer) {
    // Validate that it was in fact a
    if (!ACCESS_MODIFIERS.contains(lexer.current().representation()))
      throw new NotImplementedException();

    this.accessModifier = lexer.readCurrentAndAdvance(KeywordToken.class);
  }

  public static AccessModifierExpression read(VerbaExpression expression, Lexer lexer) {
    return new AccessModifierExpression(expression, lexer);
  }

  public LexInfo accessModifier() {
    return this.accessModifier;
  }

  @Override
  public void accept(ExpressionTreeVisitor visitor) {

  }
}
