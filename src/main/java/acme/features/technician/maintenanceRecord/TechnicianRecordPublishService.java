
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
			else {
				aircraftId = Integer.valueOf(-1);
				status = false;
			}
			if (!aircraftId.equals(Integer.valueOf(0))) {
				aircraft = this.repository.findAircraftById(aircraftId);
				if (aircraft == null)
					status = false;
			}
			//comprobacion de la manipulacion de la fecha
			//añadir en el create
			if (super.getRequest().hasData("maintanenceMoment")) {
				Integer recordId = Integer.valueOf(super.getRequest().getData("id", String.class).trim());
				Date fechaFormulario = super.getRequest().getData("maintanenceMoment", Date.class);
				Date fechaBD = this.repository.findRecordById(recordId).getMaintanenceMoment();
				if (!fechaFormulario.equals(fechaBD))
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

		super.bindObject(record, "maintanenceMoment", "status", "aircraft", "nextMaintanence", "estimatedCost", "notes");

	}

	@Override
	public void validate(final MaintanenceRecord record) {
		//debe tener al menos una task publicada

		Collection<Task> tasks = this.repository.findTasksByRecordId(record.getId());

		boolean allTasksPublished = tasks.stream().allMatch(task -> !task.isDraftMode());
		boolean atLeastOneTask = !tasks.isEmpty();

		super.state(allTasksPublished, "*", "technician.maintanence-record.error.unpublishedTask.message");
		super.state(atLeastOneTask, "*", "technician.maintanence-record.error.noPublishedTasks.message");

	}
	@Override
	public void perform(final MaintanenceRecord record) {
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
