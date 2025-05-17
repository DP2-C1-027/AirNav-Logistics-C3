
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
		boolean status = true;
		Technician tech;

		Task task;
		if (super.getRequest().hasData("id", int.class)) {
			Integer taskId;
			try {
				taskId = super.getRequest().getData("id", int.class);
			} catch (Exception e) {
				taskId = null;
			}

			task = taskId != null ? this.repository.findTaskById(taskId) : null;
			tech = task == null ? null : task.getTechnician();
			status = tech == null ? false : super.getRequest().getPrincipal().hasRealm(tech) || task != null && !task.isDraftMode();
		} else
			status = false;

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
		super.addPayload(dataset, task, "type", "draftMode", "description", "priority", "estimatedDuration");
		super.getResponse().addData(dataset);
	}
}
