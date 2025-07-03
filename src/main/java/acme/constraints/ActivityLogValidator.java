
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.flightAssignment.ActivityLog;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	// Internal State ----------------------------------------------------

	// Initialiser ------------------------------------------------------------

	@Override
	public void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	// AbstractValidator interface --------------------------------------------

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {
		assert context != null;

		if (activityLog.getFlightAssignment() != null && activityLog.getFlightAssignment().getLeg() != null) {
			// Check if the activity log is related with a flight assignment with completed leg
			boolean isLegCompleted = activityLog.getFlightAssignment().getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment());
			super.state(context, isLegCompleted, "flightAssignment", "acme.validation.activityLog.legCompleted");

			// Check if the activity log moment is after the flight assignment last updated
			// There cannot be an activity log whose registration moment is before a flight assignment last update date
			boolean isAfterFlightAssignmentLastUpdate = activityLog.getRegistrationMoment().after(activityLog.getFlightAssignment().getMoment());
			super.state(context, isAfterFlightAssignmentLastUpdate, "flightAssignment", "acme.validation.activityLog.createdfterFlightAssignmentLastUpdate");

			// Check if the activity log moment is after the leg scheduled arrival
			// There cannot be an activity log whose registration moment is before a leg scheduled arrival
			boolean isAfterLegScheduledArrival = activityLog.getRegistrationMoment().after(activityLog.getFlightAssignment().getLeg().getScheduledArrival());
			super.state(context, isAfterLegScheduledArrival, "leg", "acme.validation.activityLog.createdAfterLegScheduledArrival");

			// Check if the leg related is published
			boolean isLegPublished = activityLog.getFlightAssignment().getLeg().isDraftMode();
			super.state(context, !isLegPublished, "leg", "acme.validation.activityLog.legPublished");
		}

		return !super.hasErrors(context);
	}

}
