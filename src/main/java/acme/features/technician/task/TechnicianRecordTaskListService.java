
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.Task;
import acme.entities.maintanenceRecords.TaskType;
import acme.realms.Technician;

@GuiService
public class TechnicianRecordTaskListService extends AbstractGuiService<Technician, Task> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		int technicianId;
		int taskId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = super.getRequest().getPrincipal().hasRealm(this.repository.findTechnicianById(technicianId));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Task> task;
		Technician tech;
		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		int id;
		MaintanenceRecord record;

		id = super.getRequest().getData("recordId", int.class);
		record = this.repository.findRecordById(id);

		task = this.repository.findTasksByTechId(record.getId(), tech.getId());

		super.getBuffer().addData(task);
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
