
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@Pattern(regexp = "^$|^\\+?\\d{6,15}$") // Permite cadenas vacías o números de teléfono válidos
public @interface ValidPhoneNumberOptional {

	String message() default "{acme.validation.text.message}"; // Mensaje de error personalizado
	Class<?>[] groups() default {}; // Grupos de validación
	Class<? extends Payload>[] payload() default {}; // Payload para categorizar errores
}
