
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintanenceRecords.InvolvedIn;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianInvolvedInServiceShow extends AbstractGuiService<Technician, InvolvedIn> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvedInRepository repository;


	// AbstractService<Manager, ProjectUserStoryLink> ---------------------------
	@Override
	public void authorise() {
		Technician tech;
		boolean status = true;

		if (super.getRequest().hasData("id", int.class)) {
			Integer id;
			try {
				id = super.getRequest().getData("id", int.class);
			} catch (Exception e) {
				id = null;
			}
			InvolvedIn bRecord = id != null ? this.repository.findInvolvedIn(id) : null;
			MaintanenceRecord record = id != null ? this.repository.findOneRecordByInvolvedIn(id) : null;
			Task task = id != null ? this.repository.findOneTaskByInvolvedIn(id) : null;
			tech = record != null ? record.getTechnician() : null;
			status = tech == null ? false : super.getRequest().getPrincipal().hasRealm(tech) || bRecord != null && tech != null && !record.isDraftMode() && task != null && !task.isDraftMode();
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		InvolvedIn involved = this.repository.findInvolvedIn(id);

		super.getBuffer().addData(involved);
	}

	@Override
	public void unbind(final InvolvedIn involved) {
		Dataset dataset;
		SelectChoices recordChoices;
		SelectChoices taskChoices;

		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		Collection<MaintanenceRecord> records = this.repository.findRecordByTechnicianId(technician.getId());
		Collection<Task> tasks = this.repository.findTaskByTechnicianId(technician.getId());

		recordChoices = SelectChoices.from(records, "maintanenceMoment", involved.getMaintanenceRecord());
		taskChoices = SelectChoices.from(tasks, "description", involved.getTask());

		dataset = super.unbindObject(involved, "maintanenceRecord", "task");
		dataset.put("maintanenceRecord", recordChoices);
		dataset.put("task", taskChoices);
		dataset.put("draftMode", involved.getMaintanenceRecord().isDraftMode());
		super.addPayload(dataset, involved, "maintanenceRecord", "task");

		super.getResponse().addData(dataset);

	}

}
