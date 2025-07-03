
package acme.constraints;

import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.realms.Technician;
import acme.realms.TechnicianRepository;

@Validator
public class TechnicianValidator extends AbstractValidator<ValidTechnician, Technician> {

	@Override
	protected void initialise(final ValidTechnician annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Technician technician, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result = true;

		if (technician == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = false;
		} else if (technician.getCodigo() != null) {
			TechnicianRepository repository = SpringHelper.getBean(TechnicianRepository.class);

			Optional<Technician> sameLicense = repository.findByLicenseNumber(technician.getCodigo(), technician.getId());

			if (sameLicense.isPresent()) {
				super.state(context, false, "codigo", "javax.validation.constraints.Unique.message");
				result = false;
			}
		}

		result = result && !super.hasErrors(context);

		return result;
	}

}
