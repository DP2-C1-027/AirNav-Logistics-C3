
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
import acme.entities.maintanenceRecords.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianRecordPublishService extends AbstractGuiService<Technician, MaintanenceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		MaintanenceRecord record;
		Technician tech;

		if (super.getRequest().hasData("id", int.class)) {
			Integer recordId;
			try {
				recordId = super.getRequest().getData("id", int.class);
			} catch (Exception e) {
				recordId = null;
			}
			record = recordId != null ? this.repository.findRecordById(recordId) : null;
			tech = record == null ? null : record.getTechnician();
			status = tech == null ? false : record != null && record.isDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);
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

		super.bindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes");

	}

	@Override
	public void validate(final MaintanenceRecord record) {
		//debe tener al menos una task publicada

		Collection<Task> tasks = this.repository.findTasksByRecordId(record.getId());

		boolean allTasksPublished = tasks.isEmpty() || tasks.stream().allMatch(task -> !task.isDraftMode());
		boolean atLeastOnePublished = tasks.stream().anyMatch(task -> !task.isDraftMode());

		super.state(allTasksPublished, "*", "technician.maintanence-record.error.unpublishedTask.message");
		super.state(atLeastOnePublished, "*", "technician.maintanence-record.error.noPublishedTasks.message");

	}
	@Override
	public void perform(final MaintanenceRecord record) {
		assert record != null;
		Date ahora = MomentHelper.getCurrentMoment();
		record.setMaintanenceMoment(ahora);
		record.setDraftMode(false);
		this.repository.save(record);
	}

	@Override
	public void unbind(final MaintanenceRecord record) {

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
		super.getResponse().addData(dataset);
	}
}
