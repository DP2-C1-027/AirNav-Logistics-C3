
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
		Technician tech;

		MaintanenceRecord record;
		if (super.getRequest().hasData("recordId", int.class)) {
			Integer recordId;
			String isInteger;
			isInteger = super.getRequest().getData("recordId", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				recordId = Integer.valueOf(isInteger);
			else
				recordId = null;
			record = recordId != null ? this.repository.findRecordById(recordId) : null;
			tech = record != null ? record.getTechnician() : null;
			status = tech != null && super.getRequest().getPrincipal().hasRealm(tech);
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

		task = this.repository.findTasksByTechId(record.getId());

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
