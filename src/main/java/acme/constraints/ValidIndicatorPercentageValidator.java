
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claims.Indicator;
import acme.entities.claims.TrackingLog;

@Validator
public class ValidIndicatorPercentageValidator extends AbstractValidator<ValidIndicatorPercentage, TrackingLog> {

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		if (trackingLog.getResolutionPercentage() != null) {
			Double percentage = trackingLog.getResolutionPercentage();
			Indicator indicator = trackingLog.getIndicator();

			if (percentage == 100.0) {
				if (indicator != Indicator.ACCEPTED && indicator != Indicator.REJECTED) {
					this.state(context, false, "indicator", "acme.validation.tracking.indicator.must.be.accepted.or.rejected");
					return false;
				}
			} else if (indicator != Indicator.PENDING) {
				this.state(context, false, "indicator", "acme.validation.tracking.indicator.must.be.pending");
				return false;
			}

			return true;
		}
		return false;
	}
}
