package com.verba.language.parse.tokens.operators.mathop;

import com.verba.language.parse.codestream.CodeStream;
import com.verba.language.parse.tokenization.Token;
import com.verba.language.parse.tokens.lambda.LambdaToken;
import com.verba.language.parse.tokens.operators.assignment.AssignmentToken;
import com.verba.language.parse.tokens.operators.comparison.CompositeComparisonToken;
import com.verba.language.parse.tokens.operators.tags.TagToken;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Created by sircodesalot on 14-2-17.
 */
public class OperatorToken implements Token {
  private static final Set<Character> operators = ((Supplier<Set<Character>>) () -> {
    Character[] operators = new Character[]{
      '+', '-', '%', '*', '^', '#',
      '/', '>', '<', '=', ',', '@',
      '&', '|', '.', ':', ';', '!'};

    Set<Character> operatorSet = new HashSet<Character>();
    for (Character keyword : operators) operatorSet.add(keyword);

    return operatorSet;
  }).get();

  private final String representation;

  protected OperatorToken(Character representation) {
    this.representation = representation.toString();
  }

  @Override
  public String toString() {
    return this.representation;
  }

  public static boolean isOperatorToken(Character text) {
    return OperatorToken.operators.contains(text);
  }

  public static OperatorToken read(CodeStream stream) {
    Character firstToken = stream.read();

    if (LambdaToken.isLambdaToken(firstToken, stream))
      return LambdaToken.read(firstToken, stream);

    else if (TagToken.isTagToken(firstToken, stream))
      return TagToken.read(firstToken, stream);

    else if (AssignmentToken.isAssignmentToken(firstToken, stream))
      return AssignmentToken.read(firstToken, stream);

    else if (CompositeComparisonToken.isCompositeComparisonToken(firstToken, stream))
      return CompositeComparisonToken.read(firstToken, stream);

    else if (InfixOperatorToken.isMathOpToken(firstToken))
      return InfixOperatorToken.cast(firstToken);


    else return new OperatorToken(firstToken);
  }
}
