
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claims.TrackingLog;
import acme.entities.claims.TrackingLogRepository;

@Validator
public class ValidPercentageIncreaseValidator extends AbstractValidator<ValidPercentageIncrease, TrackingLog> {

	@Autowired
	private TrackingLogRepository trackingLogRepository;


	@Override
	public boolean isValid(final TrackingLog newTrackingLog, final ConstraintValidatorContext context) {
		// 1. Validaciones básicas de nulidad
		if (newTrackingLog == null || newTrackingLog.getClaim() == null || newTrackingLog.getResolutionPercentage() == null)
			return true;

		// 2. Obtener logs existentes ordenados por fecha ASCENDENTE (más antiguo primero)
		List<TrackingLog> existingLogs = this.trackingLogRepository.findTrackingLogsByClaimIdOrderedByDateAsc(newTrackingLog.getClaim().getId());

		// 3. Si es el primer registro para esta claim
		if (existingLogs.isEmpty())
			return true;

		// 4. Buscar el registro inmediatamente anterior (el último antes del nuevo)
		TrackingLog previousLog = existingLogs.get(existingLogs.size() - 1);

		// 5. Si estamos actualizando un registro existente, tomar el anterior a ese
		Integer newId = newTrackingLog.getId(); // Usamos Integer en lugar de int
		if (newId != null) {
			int currentIndex = -1;
			for (int i = 0; i < existingLogs.size(); i++)
				if (newId.equals(existingLogs.get(i).getId())) {
					currentIndex = i;
					break;
				}
			if (currentIndex > 0)
				previousLog = existingLogs.get(currentIndex - 1);
			else if (currentIndex == 0)
				// Es el primer registro, no hay anterior para comparar
				return true;
		}

		// 6. Validar porcentaje si hay registro anterior
		if (previousLog != null)
			return this.validatePercentage(newTrackingLog, previousLog, context);

		return true;
	}

	private boolean validatePercentage(final TrackingLog newLog, final TrackingLog previousLog, final ConstraintValidatorContext context) {
		Double previousPercentage = previousLog.getResolutionPercentage();
		Double newPercentage = newLog.getResolutionPercentage();

		if (newPercentage < previousPercentage) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(String.format("El porcentaje (%.1f%%) no puede ser menor que el del registro anterior (%.1f%%)", newPercentage, previousPercentage)).addPropertyNode("resolutionPercentage").addConstraintViolation();
			return false;
		}
		return true;
	}
}
