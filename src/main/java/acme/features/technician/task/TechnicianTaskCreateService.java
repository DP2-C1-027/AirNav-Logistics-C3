
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
public class TechnicianTaskCreateService extends AbstractGuiService<Technician, Task> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


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

		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		task = new Task();
		task.setTechnician(tech);
		super.getBuffer().addData(task);

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

	}

	@Override
	public void unbind(final Task task) {

		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "draftMode", "description", "priority", "estimatedDuration");
		//dataset.put("draftMode", task.getDraftMode());
		dataset.put("type", choices);
		super.getResponse().addData(dataset);

	}
}
