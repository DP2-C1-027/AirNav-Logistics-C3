
package acme.constraints;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;

@Validator
public class PromotionCodeValidator extends AbstractValidator<ValidPromotionCode, String> {

	private static final Pattern PROMO_CODE_PATTERN = Pattern.compile("^[A-Z]{4}-[0-9]{2}$");


	@Override
	public void initialize(final ValidPromotionCode constraintAnnotation) {

	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		// Permitir valor nulo o vacío
		if (value == null || value.isEmpty())
			return true;

		// Validar longitud mínima antes de usar substring
		if (value.length() < 2)
			return false;

		// Obtener año actual desde MomentHelper
		Date currentDate = MomentHelper.getCurrentMoment();
		Instant instant = currentDate.toInstant();
		ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
		int actualYearTwoDigits = zonedDateTime.getYear() % 100;

		// Obtener últimos dos dígitos del valor
		String lastTwoDigits = value.substring(value.length() - 2);

		return PromotionCodeValidator.PROMO_CODE_PATTERN.matcher(value).matches() && String.format("%02d", actualYearTwoDigits).equals(lastTwoDigits);
	}
}
