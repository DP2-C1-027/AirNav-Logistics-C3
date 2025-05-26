
package acme.features.technician.maintenanceRecord;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.StatusMaintanenceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianRecordUpdateService extends AbstractGuiService<Technician, MaintanenceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianRecordRepository repository;

	// AbstractService -------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		MaintanenceRecord record;
		Technician tech;
		Aircraft aircraft;

		if (super.getRequest().hasData("id")) {
			Integer recordId;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				recordId = Integer.valueOf(isInteger);
			else
				recordId = null;
			record = recordId != null ? this.repository.findRecordById(recordId) : null;
			tech = record != null ? record.getTechnician() : null;
			status = tech != null && record.isDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);
		}

		if (super.getRequest().hasData("aircraft")) {
			Integer aircraftId;
			String isInteger;

			isInteger = super.getRequest().getData("aircraft", String.class);
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				aircraftId = Integer.valueOf(isInteger);
			else
				aircraftId = Integer.valueOf(-1);

			if (!aircraftId.equals(Integer.valueOf(0))) {
				aircraft = this.repository.findAircraftById(aircraftId);
				if (aircraft == null)
					status = false;
			}
		} else
			status = false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintanenceRecord record;
		int id;

		id = super.getRequest().getData("id", int.class);
		record = this.repository.findRecordById(id);

		super.getBuffer().addData(record);
	}

	@Override
	public void bind(final MaintanenceRecord record) {

		super.bindObject(record, "status", "nextMaintanence", "estimatedCost", "notes", "aircraft");

	}

	@Override
	public void validate(final MaintanenceRecord record) {

	}

	@Override
	public void perform(final MaintanenceRecord record) {
		Date ahora = MomentHelper.getCurrentMoment();
		record.setMaintanenceMoment(ahora);
		this.repository.save(record);
	}

	@Override
	public void unbind(final MaintanenceRecord record) {

		Dataset dataset;
		SelectChoices choices;
		SelectChoices aircraftChoices;
		Collection<Aircraft> aircrafts;
		Date ahora = MomentHelper.getCurrentMoment();
		record.setMaintanenceMoment(ahora);
		aircrafts = this.repository.getAllAircraft();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", record.getAircraft());
		choices = SelectChoices.from(StatusMaintanenceRecord.class, record.getStatus());
		dataset = super.unbindObject(record, "maintanenceMoment", "aircraft", "status", "nextMaintanence", "estimatedCost", "notes", "draftMode");
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("status", choices);
		super.addPayload(dataset, record, "status", "nextMaintanence", "estimatedCost", "notes", "aircraft");
		super.getResponse().addData(dataset);
	}
}
