
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
		boolean status;
		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		int id = super.getRequest().getData("id", int.class);
		InvolvedIn involved = this.repository.findInvolvedIn(id);
		status = involved != null && super.getRequest().getPrincipal().hasRealm(tech);
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

		dataset.put("draftMode", involved.getMaintanenceRecord().getDraftMode());
		//no se si es el record o el task lo que tengo que mirar aqui

		super.getResponse().addData(dataset);

	}

}
