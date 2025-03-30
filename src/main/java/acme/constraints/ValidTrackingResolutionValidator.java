
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claims.Indicator;
import acme.entities.claims.TrackingLog;

@Validator
public class ValidTrackingResolutionValidator extends AbstractValidator<ValidTrackingResolution, TrackingLog> {

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		Indicator indicator = trackingLog.getIndicator();

		if (!(indicator == Indicator.ACCEPTED || indicator == Indicator.REJECTED))
			return true;

		boolean isValid = trackingLog.getResolutionDetails() != null && !trackingLog.getResolutionDetails().trim().isEmpty();

		if (!isValid)
			this.state(context, false, "resolutionDetails", "acme.validation.tracking.resolution.required");

		return isValid;
	}
}
