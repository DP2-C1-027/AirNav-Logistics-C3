
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintanenceRecords.InvolvedIn;
import acme.entities.maintanenceRecords.Task;
import acme.features.technician.involvedIn.TechnicianInvolvedInRepository;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskDeleteService extends AbstractGuiService<Technician, Task> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository		repository;

	@Autowired
	private TechnicianInvolvedInRepository	involvedRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		Task task;
		Technician tech;
		if (super.getRequest().hasData("id", int.class)) {
			Integer taskId;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				taskId = Integer.valueOf(isInteger);
			else
				taskId = null;
			task = taskId != null ? this.repository.findTaskById(taskId) : null;
			tech = task != null ? task.getTechnician() : null;
			status = tech != null && task.isDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);
		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Task task;
		int id;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);

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
		Collection<InvolvedIn> involved;

		involved = this.repository.findAllInvolvedInById(task.getId());
		this.involvedRepository.deleteAll(involved);
		this.repository.delete(task);

	}

	/*
	 * @Override
	 * public void unbind(final Task task) {
	 * Dataset dataset;
	 * SelectChoices choices;
	 * choices = SelectChoices.from(TaskType.class, task.getType());
	 * 
	 * dataset = super.unbindObject(task, "type", "draftMode", "description", "priority", "estimatedDuration");
	 * dataset.put("type", choices.getSelected().getKey());
	 * dataset.put("types", choices);
	 * super.addPayload(dataset, task, "type", "draftMode", "description", "priority", "estimatedDuration");
	 * 
	 * super.getResponse().addData(dataset);
	 * }
	 */

}
