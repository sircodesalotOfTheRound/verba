package com.verba.language.graph.validation.validation;


import com.javalinq.interfaces.QIterable;
import com.verba.language.graph.validation.violations.ValidationViolation;
import com.verba.language.graph.validation.violations.ValidationViolationList;
import com.verba.language.parsing.expressions.VerbaExpression;

/**
 * Created by sircodesalot on 14-5-3.
 */
public abstract class ExpressionValidator<T> {
  private final T target;
  private final ValidationViolationList violations = new ValidationViolationList();

  public ExpressionValidator(T target) {
    this.target = target;
  }

  protected void addViolations(Iterable<ValidationViolation> violations) {
    for (ValidationViolation violation : violations) {
      this.addViolation(violation);
    }
  }

  protected void addViolation(ValidationViolation violation) {
    this.violations.add(violation);
  }

  protected void addError(VerbaExpression expression, String format, Object... args) {
    this.violations.addError(expression, format, args);
  }

  protected void addWarning(VerbaExpression expression, String format, Object... args) {
    this.violations.addWarning(expression, format, args);
  }

  public QIterable<ValidationViolation> violations() {
    return this.violations;
  }

  protected T target() {
    return this.target;
  }
}