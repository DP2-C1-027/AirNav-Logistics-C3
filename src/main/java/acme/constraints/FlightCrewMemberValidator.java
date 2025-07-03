
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.flightcrewmember.FlightCrewMember;
import acme.realms.flightcrewmember.FlightCrewMemberRepository;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	// Internal State ----------------------------------------------------

	@Autowired
	private FlightCrewMemberRepository repository;

	// Initialiser ------------------------------------------------------------


	@Override
	public void initialise(final ValidFlightCrewMember annotation) {
		assert annotation != null;
	}

	// AbstractValidator interface --------------------------------------------

	@Override
	public boolean isValid(final FlightCrewMember flightCrewMember, final ConstraintValidatorContext context) {
		assert context != null;

		// Check if the code related with the flight crew member is already used by another flight crew member
		FlightCrewMember existingFlightCrewMember = this.repository.findFlightCrewMemberCode(flightCrewMember.getCodigo());
		boolean uniqueFlightCrewMember = existingFlightCrewMember == null || existingFlightCrewMember.equals(flightCrewMember);
		super.state(context, uniqueFlightCrewMember, "codigo", "acme.validation.identifier.duplicated");

		// Check if the initials match with the flight crew member initials
		DefaultUserIdentity identity = flightCrewMember.getIdentity();
		boolean isValid = this.initialsMatchCrewMember(flightCrewMember.getCodigo(), identity.getName(), identity.getSurname());
		super.state(context, isValid, "codigo", "acme.validation.identifier.mismatch");

		return !super.hasErrors(context);
	}

	private boolean initialsMatchCrewMember(final String code, final String name, final String surname) {
		// Get from original string a substring form with the letters part
		String lettersPart = code.substring(0, code.length() - 6);

		// - If there are 2 letters: First from name + First from surname.
		// - If there are 3 letters: First from name + First from surname + Second from surname.
		for (int i = 0; i < lettersPart.length(); i++) {
			char expectedChar;
			switch (i) {
			case 0:
				expectedChar = name.charAt(0);
				break;
			case 1:
				expectedChar = surname.charAt(0);
				break;
			case 2:
				expectedChar = surname.charAt(1);
				break;
			default:
				return false;
			}

			if (Character.toUpperCase(lettersPart.charAt(i)) != Character.toUpperCase(expectedChar))
				return false;
		}

		return true;
	}
}
