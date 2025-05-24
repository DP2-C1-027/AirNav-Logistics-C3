
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianRecordTaskListService extends AbstractGuiService<Technician, Task> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status = true;

		MaintanenceRecord record;

		if (super.getRequest().hasData("recordId", int.class)) {
			Integer id;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (isInteger != null && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				id = Integer.valueOf(isInteger);
			else
				id = Integer.valueOf(-1);
			record = id != null ? this.repository.findRecordById(id) : null;
			Technician tech = record != null ? record.getTechnician() : null;

			status = tech == null ? false : super.getRequest().getPrincipal().hasRealm(tech) || record != null && !record.isDraftMode();
		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Task> task;

		int id;
		MaintanenceRecord record;

		id = super.getRequest().getData("recordId", int.class);
		record = this.repository.findRecordById(id);

		task = this.repository.findTasksByTechId(record.getId(), record.getTechnician().getId());

		super.getBuffer().addData(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		dataset = super.unbindObject(task, "type", "draftMode", "description", "priority", "estimatedDuration");
		super.addPayload(dataset, task, "type", "draftMode", "description", "priority", "estimatedDuration");
		super.getResponse().addData(dataset);
	}
}
