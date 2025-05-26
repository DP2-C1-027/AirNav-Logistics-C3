
package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintanenceRecords.InvolvedIn;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.Task;
import acme.entities.maintanenceRecords.TaskType;
import acme.features.technician.maintenanceRecord.TechnicianRecordRepository;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskCreateService2 extends AbstractGuiService<Technician, Task> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository	repository;

	@Autowired
	private TechnicianRecordRepository	recordRepository;

	// AbstractGuiService interface ------------------------------------------

	InvolvedIn							involved	= new InvolvedIn();


	@Override
	public void authorise() {
		boolean status = true;
		if (super.getRequest().hasData("recordId", int.class)) {
			Integer recordId;
			try {
				recordId = super.getRequest().getData("recordId", int.class);
			} catch (Exception e) {
				recordId = null;

			}

			MaintanenceRecord record = recordId != null ? this.recordRepository.findRecordById(recordId) : null;
			Technician tech = record != null ? record.getTechnician() : null;
			status = tech != null && record.isDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);
		} else
			status = false;
		if (super.getRequest().hasData("id")) {
			Integer id;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();

			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				id = Integer.valueOf(isInteger);
			else
				id = Integer.valueOf(-1);

			if (id != 0)
				status = false;

		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Task task;
		int recordId = super.getRequest().getData("recordId", int.class);
		MaintanenceRecord record = this.recordRepository.findRecordById(recordId);

		task = new Task();
		task.setTechnician(record.getTechnician());
		task.setDraftMode(true);
		super.getBuffer().addData(task);
		super.getResponse().addGlobal("recordId", recordId);
		this.involved.setMaintanenceRecord(record);

	}

	@Override
	public void bind(final Task task) {

		super.bindObject(task, "type", "description", "priority", "estimatedDuration");

	}

	@Override
	public void validate(final Task task) {

		;
	}

	@Override
	public void perform(final Task task) {

		this.repository.save(task);
		this.involved.setTask(task);
		this.recordRepository.save(this.involved);

	}

	@Override
	public void unbind(final Task task) {

		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration");
		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);
		super.addPayload(dataset, task, "type", "description", "priority", "estimatedDuration");

		super.getResponse().addData(dataset);
	}
}
