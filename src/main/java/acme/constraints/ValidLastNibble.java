
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

@ReportAsSingleViolation
@Constraint(validatedBy = LastNibbleValidator.class)

public @interface ValidLastNibble {

	String message() default "{acme.validation.text.message}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
