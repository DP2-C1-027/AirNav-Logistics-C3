
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

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Task task;
		Technician tech;

		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		task = new Task();
		task.setTechnician(tech);
		task.setDraftMode(true);
		super.getBuffer().addData(task);

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
		assert task != null;
		this.repository.save(task);

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
