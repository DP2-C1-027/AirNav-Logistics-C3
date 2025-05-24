
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
public class TechnicianRecordCreateService extends AbstractGuiService<Technician, MaintanenceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		Technician tech;
		Aircraft aircraft;

		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		status = super.getRequest().getPrincipal().hasRealm(tech);
		if (super.getRequest().hasData("id")) {
			Integer id;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();

			if (isInteger != null && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				id = Integer.valueOf(isInteger);
			else
				id = Integer.valueOf(-1);
			if (!id.equals(Integer.valueOf(0)))
				status = false;

		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

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
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Technician tech;
		MaintanenceRecord record;
		Date moment;

		moment = MomentHelper.getCurrentMoment();
		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		record = new MaintanenceRecord();

		record.setTechnician(tech);
		record.setMaintanenceMoment(moment);
		record.setDraftMode(true);

		super.getBuffer().addData(record);

	}

	@Override
	public void bind(final MaintanenceRecord record) {
		int aircraftId = super.getRequest().getData("aircraft", int.class);

		Aircraft aircraft = this.repository.findAircraftById(aircraftId);
		super.bindObject(record, "maintanenceMoment", "aircraft", "status", "nextMaintanence", "estimatedCost", "notes");
		record.setAircraft(aircraft);

	}

	@Override
	public void validate(final MaintanenceRecord record) {
		if (record.getAircraft() == null)
			super.state(false, "aircraft", "technician.maintanence-record.error.no-aircraft");
	}

	@Override
	public void perform(final MaintanenceRecord record) {
		assert record != null;
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
		dataset = super.unbindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes");
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("status", choices);
		super.addPayload(dataset, record, "aircraft", "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes");

		super.getResponse().addData(dataset);

	}
}
