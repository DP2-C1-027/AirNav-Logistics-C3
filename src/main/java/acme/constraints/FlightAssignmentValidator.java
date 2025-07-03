
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flightAssignment.FlightAssignment;
import acme.features.flightCrewMember.flightAssignment.FlightCrewMemberFlightAssignmentRepository;

@Validator
public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {

	// Internal State ----------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// Initialiser ------------------------------------------------------------


	@Override
	public void initialise(final ValidFlightAssignment annotation) {
		assert annotation != null;
	}

	// AbstractValidator interface --------------------------------------------

	@Override
	public boolean isValid(final FlightAssignment flightAssignment, final ConstraintValidatorContext context) {
		assert context != null;

		if (flightAssignment.getLeg() != null) {
			if (flightAssignment.getDuty() != null) {
				// Check if there are more than one pilot or one co-pilot in a leg related
				boolean isDutyAlreadyAssigned = this.repository.hasDutyAssigned(flightAssignment.getId(), flightAssignment.getLeg().getId(), flightAssignment.getDuty());
				super.state(context, !isDutyAlreadyAssigned, "duty", "acme.validation.flightAssignment.duty");
			}

			// Check if there is a time frame conflict between flight assignments
			boolean isAlreadyAssigned = this.repository.hasConflictingFlightAssignment(flightAssignment.getId(), flightAssignment.getFlightCrewMember().getId(), flightAssignment.getLeg().getScheduledDeparture(),
				flightAssignment.getLeg().getScheduledArrival());
			super.state(context, !isAlreadyAssigned, "leg", "acme.validation.flightAssignment.flightCrewMember.multipleLegs");

			// Check if the flight assignment moment is before the leg start
			// There cannot be a flight assignment whose last update date is after the start date of a leg
			boolean isBeforeLegStart = flightAssignment.getMoment().before(flightAssignment.getLeg().getScheduledDeparture());
			super.state(context, isBeforeLegStart, "leg", "acme.validation.flightAssignment.createdBeforeLegStart");

			// Check if the leg related is published
			boolean legPublished = flightAssignment.getLeg().isDraftMode();
			super.state(context, !legPublished, "leg", "acme.validation.flightAssignment.legPublished");
		}

		return !super.hasErrors(context);
	}

}
