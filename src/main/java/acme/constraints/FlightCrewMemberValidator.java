
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.StringHelper;
import acme.realms.flightcrewmember.FlightCrewMember;
import acme.realms.flightcrewmember.FlightCrewMemberRepository;

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

		if (flightCrewMember == null || flightCrewMember.getCodigo() == null || flightCrewMember.getIdentity() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (StringHelper.isBlank(flightCrewMember.getCodigo()))
			super.state(context, false, "identifier", "javax.validation.constraints.NotBlank.message");
		else {
			FlightCrewMember existingFlightCrewMember = this.repository.findFlightCrewMemberCode(flightCrewMember.getCodigo());

			boolean uniqueFlightCrewMember = existingFlightCrewMember == null || existingFlightCrewMember.equals(flightCrewMember);
			super.state(context, uniqueFlightCrewMember, "codigo", "acme.validation.identifier.duplicated");

			DefaultUserIdentity identity = flightCrewMember.getIdentity();
			String initials = "" + identity.getName().charAt(0) + identity.getSurname().charAt(0);

			boolean containsInitials = StringHelper.startsWith(flightCrewMember.getCodigo(), initials, false);
			super.state(context, containsInitials, "codigo", "acme.validation.identifier.mismatch");
		}

		return !super.hasErrors(context);
	}
}
