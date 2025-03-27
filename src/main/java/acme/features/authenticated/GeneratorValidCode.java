
package acme.features.authenticated;

import acme.client.components.basis.AbstractRole;
import acme.client.components.principals.DefaultUserIdentity;

public class GeneratorValidCode {

	public static String generateValidCode(final AbstractRole role) {
		if (role == null || role.getIdentity() == null)
			throw new IllegalArgumentException("Role or identity must not be null");

		DefaultUserIdentity identity = role.getIdentity();

		// Extraer el nombre y apellido
		String name = identity.getName();
		String surname = identity.getSurname();

		if (name == null || surname == null || name.isEmpty() || surname.isEmpty())
			throw new IllegalArgumentException("Name and surname must not be empty");

		// Extraer iniciales (dos o tres letras)
		String initials = name.substring(0, 1).toUpperCase() + surname.substring(0, 1).toUpperCase();

		// Verificar si el código debe tener una tercera letra
		String codePrefix = initials;
		if (surname.length() > 1)
			// Solo agregar la tercera letra si el código va a tener 3 letras iniciales
			if (codePrefix.length() == 2)
				initials += surname.substring(1, 2).toUpperCase();

		// Generar seis números aleatorios (rellenando con ceros si es necesario)
		int randomNumber = (int) (Math.random() * 1_000_000);
		String numberPart = String.format("%06d", randomNumber);

		// Devolver el código completo
		//dejo q el cliente añade el numero
		return initials;
	}

}
