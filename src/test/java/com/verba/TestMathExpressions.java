package com.verba;

import com.verba.language.parse.expressions.blockheader.varname.NamedValueExpression;
import com.verba.language.parse.expressions.categories.RValueExpression;
import com.verba.language.parse.expressions.rvalue.simple.InfixExpression;
import com.verba.language.parse.expressions.rvalue.simple.NumericExpression;
import com.verba.language.parse.lexing.Lexer;
import com.verba.language.parse.tokens.operators.mathop.AddOpToken;
import com.verba.language.parse.tokens.operators.mathop.SubtractOpToken;
import com.verba.tools.TestTools;
import org.junit.Test;

/**
 * Created by sircodesalot on 15/2/17.
 */
public class TestMathExpressions {
  @Test
  public void testMathExpressions() {
    //            +
    //         /       \
    //  variable       -
    //              /      \
    //             4  Class.function()

    Lexer lexer = TestTools.generateLexerFromString("variable + 4 - Class.function()");
    InfixExpression expression = (InfixExpression) RValueExpression.read(null, lexer);

    assert(expression.lhs().is(NamedValueExpression.class));
    assert(expression.operator().is(AddOpToken.class));
    assert(expression.rhs().is(InfixExpression.class));

    InfixExpression rhs = (InfixExpression) expression.rhs();
    assert(rhs.lhs().is(NumericExpression.class));
    assert(rhs.operator().is(SubtractOpToken.class));
    assert(rhs.rhs().is(NamedValueExpression.class));
  }
}