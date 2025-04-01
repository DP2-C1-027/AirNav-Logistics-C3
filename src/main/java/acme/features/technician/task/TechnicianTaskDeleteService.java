
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
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
		boolean status;
		int taskId;
		Task task;
		Technician tech;

		taskId = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(taskId);
		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		status = task != null && task.getDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);

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

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");

		super.getResponse().addData(dataset);
	}
}
