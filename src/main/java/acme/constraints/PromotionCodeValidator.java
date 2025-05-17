
package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.features.administrator.service.AdministratorServiceRepository;

@Validator
public class PromotionCodeValidator extends AbstractValidator<ValidPromotionCode, String> {

	private static final Pattern			PROMO_CODE_PATTERN	= Pattern.compile("^[A-Z]{4}-[0-9]{2}$");

	@Autowired
	private AdministratorServiceRepository	repository;


	@Override
	public void initialize(final ValidPromotionCode constraintAnnotation) {

	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		// Permitir valor nulo o vacío
		if (value == null || value.isEmpty() || value.equals(""))
			return true;

		// Si el valor está presente, debe tener exactamente 4 caracteres
		return PromotionCodeValidator.PROMO_CODE_PATTERN.matcher(value).matches(); //&& this.repository.findByPromotionCode(value) == null;
	}
}
