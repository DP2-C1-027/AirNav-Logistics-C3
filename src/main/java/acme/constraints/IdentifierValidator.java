
package acme.constraints;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.basis.AbstractRole;
import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class IdentifierValidator extends AbstractValidator<ValidIdentifier, AbstractRole> {

	private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^([A-Z]{2,3})(\\d{6})$");


	@Override
	public boolean isValid(final AbstractRole role, final ConstraintValidatorContext context) {
		if (role == null || role.getIdentity() == null)
			return false;

		DefaultUserIdentity identity = role.getIdentity();
		String identifier = this.getIdentifierFromRole(role);

		if (identifier == null)
			return false;

		Matcher matcher = IdentifierValidator.IDENTIFIER_PATTERN.matcher(identifier);
		if (!matcher.matches()) {
			this.state(context, false, "identifier", "acme.validation.identifier.pattern");
			return false;
		}

		String identifierInitials = matcher.group(1);
		String expectedInitials = this.getInitials(identity.getName(), identity.getSurname());

		boolean isValid = identifierInitials.equals(expectedInitials);
		if (!isValid)
			this.state(context, false, "identifier", "acme.validation.identifier.mismatch");

		return isValid;
	}

	private String getInitials(final String name, final String surname) {
		if (name == null || surname == null || name.isEmpty() || surname.isEmpty())
			return "";

		return name.substring(0, 1).toUpperCase() + surname.substring(0, 1).toUpperCase();
	}

	private String getIdentifierFromRole(final AbstractRole role) {
		try {
			return (String) role.getClass().getMethod("getCode").invoke(role);
		} catch (Exception e) {
			return null;
		}
	}

}
