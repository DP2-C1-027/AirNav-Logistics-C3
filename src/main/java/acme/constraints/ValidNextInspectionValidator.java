
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;

@Validator
public class ValidNextInspectionValidator extends AbstractValidator<ValidNextInspection, Date> {

	@Override
	protected void initialise(final ValidNextInspection constraintAnnotation) {
	}

	@Override
	public boolean isValid(final Date nextInspection, final ConstraintValidatorContext context) {
		if (nextInspection == null)
			return true;
		Date fechaPasada = MomentHelper.getCurrentMoment();
		boolean isValid = nextInspection.after(fechaPasada);

		return isValid;
	}
}
