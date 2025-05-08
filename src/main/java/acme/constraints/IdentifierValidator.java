
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class IdentifierValidator extends AbstractValidator<ValidIdentifier, AbstractRealm> {

	@Override
	protected void initialise(final ValidIdentifier annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AbstractRealm realm, final ConstraintValidatorContext context) {

		String codigo = this.getIdentifierFromRealm(realm);

		boolean result;

		DefaultUserIdentity identity = realm.getIdentity();

		if (codigo != null && !codigo.isEmpty()) {
			boolean cod = codigo.matches("^([A-Z]{2,3})(\\d{6})$");

			//2 letras
			String codValido1 = "";
			//3 letras
			String codValido2 = "";
			if (codigo.length() > 2)
				codValido1 = codigo.substring(0, 2);
			if (codigo.length() > 3)
				codValido2 = codigo.substring(0, 3);

			String expectedInitials2letras = this.getInitials(identity.getName(), identity.getSurname());
			String expectedInitials3letras = this.getInitials1(identity.getName(), identity.getSurname());

			boolean cod1 = expectedInitials2letras.matches(codValido1) || expectedInitials3letras.matches(codValido2);

			super.state(context, cod1 && cod, "codigo", "acme.validation.identifier.mismatch");
		} else
			super.state(context, false, "codigo", "acme.validation.identifier.notnull");
		result = !super.hasErrors(context);

		return result;
	}

	private String getInitials(final String name, final String surname) {
		if (name == null || surname == null || name.isEmpty() || surname.isEmpty())
			return "";

		return name.substring(0, 1).toUpperCase() + surname.substring(0, 1).toUpperCase();
	}

	private String getInitials1(final String name, final String surname) {
		if (name == null || surname == null || name.isEmpty() || surname.isEmpty())
			return "";

		String initials = name.substring(0, 1).toUpperCase() + surname.substring(0, 1).toUpperCase();

		if (surname.length() > 1)

			initials += surname.substring(1, 2).toUpperCase(); // Tercera letra opcional

		return initials;
	}

	private String getIdentifierFromRealm(final AbstractRealm realm) {
		try {
			return (String) realm.getClass().getMethod("getCodigo").invoke(realm);
		} catch (Exception e) {
			return null;
		}
	}
}
