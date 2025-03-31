
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintanenceRecords.InvolvedIn;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.StatusMaintanenceRecord;
import acme.features.technician.involvedIn.TechnicianInvolvedInRepository;
import acme.realms.Technician;

@GuiService
public class TechnicianRecordDeleteService extends AbstractGuiService<Technician, MaintanenceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianRecordRepository		repository;

	@Autowired
	private TechnicianInvolvedInRepository	involvedRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		int recordId;
		MaintanenceRecord record;
		Technician tech;

		recordId = super.getRequest().getData("id", int.class);
		record = this.repository.findRecordById(recordId);
		tech = record.getTechnician() != null ? record.getTechnician() : null;
		status = record != null && record.getDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);

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
		super.bindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes");

	}

	@Override
	public void validate(final MaintanenceRecord record) {

		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(record.getDraftMode(), "draftMode", "customers.form.error.draft-mode");

	}

	@Override
	public void perform(final MaintanenceRecord record) {
		Collection<InvolvedIn> br;

		br = this.repository.findAllInvolvedInById(record.getId());
		this.involvedRepository.deleteAll(br);
		this.repository.delete(record);

	}

	@Override
	public void unbind(final MaintanenceRecord record) {
		//no se yo...
		Dataset dataset;
		SelectChoices choices;
		SelectChoices aircraftChoices;

		Collection<Aircraft> aircrafts;
		aircrafts = this.repository.getAllAircraft();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", record.getAircraft());

		choices = SelectChoices.from(StatusMaintanenceRecord.class, record.getStatus());

		dataset = super.unbindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes", "draftMode");
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("status", choices);
		// Derived attributes --------------------
		//no tengo
		super.getResponse().addData(dataset);
	}
}
