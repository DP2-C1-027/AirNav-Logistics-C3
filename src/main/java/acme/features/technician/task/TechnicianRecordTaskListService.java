
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

		boolean status;
		int technicianId;

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
		MaintanenceRecord maintenenceRecord;

		id = super.getRequest().getData("recordId", int.class);
		maintenenceRecord = this.repository.findRecordById(id);

		task = this.repository.findTasksByTechId(maintenenceRecord.getId(), tech.getId());

		super.getBuffer().addData(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		dataset = super.unbindObject(task, "type", "draftMode", "description", "priority", "estimatedDuration");
		super.getResponse().addData(dataset);
	}
}
