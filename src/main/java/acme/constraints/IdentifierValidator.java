
package acme.constraints;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class IdentifierValidator extends AbstractValidator<ValidIdentifier, AbstractRealm> {

	// Patrón para validar dos o tres letras seguidas de seis números
	private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^([A-Z]{2,3})(\\d{6})$");


	@Override
	public boolean isValid(final AbstractRealm realm, final ConstraintValidatorContext context) {
		if (realm == null || realm.getIdentity() == null)
			return false;

		DefaultUserIdentity identity = realm.getIdentity();
		String codigo = this.getIdentifierFromRealm(realm);

		if (codigo == null)
			return false;

		Matcher matcher = IdentifierValidator.IDENTIFIER_PATTERN.matcher(codigo);
		if (!matcher.matches()) {
			// Si el patrón no coincide, lanzar error en la propiedad "codigo"
			this.state(context, false, "codigo", "acme.validation.identifier.pattern");
			return false;
		}

		// Obtener las iniciales reales
		String identifierInitials = matcher.group(1);

		String expectedInitials = this.getInitials(identity.getName(), identity.getSurname(), realm);

		boolean isValid = identifierInitials.equals(expectedInitials);
		if (!isValid)
			this.state(context, false, "codigo", "acme.validation.identifier.mismatch");

		return isValid;
	}

	// Obtener las iniciales de nombre y apellido
	private String getInitials(final String name, final String surname, final AbstractRealm realm) {
		if (name == null || surname == null || name.isEmpty() || surname.isEmpty())
			return "";
		String codigo = this.getIdentifierFromRealm(realm);

		String initials = name.substring(0, 1).toUpperCase() + surname.substring(0, 1).toUpperCase();
		if (codigo.substring(0, 3).matches("[A-Z]") && surname.length() > 1)
			initials += surname.substring(1, 2).toUpperCase(); // Tercera letra opcional
		return initials;
	}

	// Extraer el código desde el realm
	private String getIdentifierFromRealm(final AbstractRealm realm) {
		try {
			return (String) realm.getClass().getMethod("getCodigo").invoke(realm);
		} catch (Exception e) {
			return null;
		}
	}
}
