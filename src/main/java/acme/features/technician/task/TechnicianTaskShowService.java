
package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintanenceRecords.Task;
import acme.entities.maintanenceRecords.TaskType;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskShowService extends AbstractGuiService<Technician, Task> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		boolean status;
		Technician tech;
		int taskId;
		Task task;

		taskId = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(taskId);
		tech = task == null ? null : task.getTechnician();
		status = super.getRequest().getPrincipal().hasRealm(tech);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int taskId;
		Task task;

		taskId = super.getRequest().getData("id", int.class);

		task = this.repository.findTaskById(taskId);

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
