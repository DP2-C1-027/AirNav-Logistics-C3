
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
	//ESTO ERA UN BOOKING RECORD


	@Override
	public void authorise() {
		boolean status;
		Technician tech;

		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		status = super.getRequest().getPrincipal().hasRealm(tech);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Task task;
		Technician tech;
		int recordId = super.getRequest().getData("recordId", int.class);
		MaintanenceRecord record = this.recordRepository.findRecordById(recordId);

		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		task = new Task();
		task.setTechnician(tech);

		super.getBuffer().addData(task);

		if (record != null)
			this.involved.setMaintanenceRecord(record);

	}

	@Override
	public void bind(final Task task) {

		super.bindObject(task, "type", "description", "priority", "estimatedDuration");
		task.setDraftMode(true);

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

		dataset = super.unbindObject(task, "type", "draftMode", "description", "priority", "estimatedDuration");
		dataset.put("type", choices);
		super.getResponse().addData(dataset);

	}
}
