
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class LastNibbleValidator extends AbstractValidator<ValidLastNibble, String> {

	@Override
	public void initialize(final ValidLastNibble constraintAnnotation) {

	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		// Permitir valor nulo o vacío
		if (value == null || value.isEmpty())
			return true;

		// Si el valor está presente, debe tener exactamente 4 caracteres
		return value.length() == 4;
	}
}
