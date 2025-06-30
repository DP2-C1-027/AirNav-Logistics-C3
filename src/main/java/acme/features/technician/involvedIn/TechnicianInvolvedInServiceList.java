
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintanenceRecords.InvolvedIn;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianInvolvedInServiceList extends AbstractGuiService<Technician, InvolvedIn> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvedInRepository repository;


	// AbstractService<Manager, ProjectUserStoryLink> ---------------------------
	@Override
	public void authorise() {
		Technician tech;
		boolean status;
		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		status = super.getRequest().getPrincipal().hasRealm(tech);
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<InvolvedIn> involved;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		involved = this.repository.findAllInvolvedInByTechnicianId(technicianId);
		//solo puede cambiar aquellos que sean los suyos (MR)
		//con todas las tasks que estén guardadas en la base de datos

		super.getBuffer().addData(involved);
	}

	@Override
	public void unbind(final InvolvedIn involved) {
		Dataset dataset;
		Task task;
		MaintanenceRecord record;
		int involvedInId = involved.getId();
		record = this.repository.findOneRecordByInvolvedIn(involvedInId);

		task = this.repository.findOneTaskByInvolvedIn(involvedInId);

		dataset = super.unbindObject(involved, "maintanenceRecord", "task");
		dataset.put("maintanenceRecord", record.getMaintanenceMoment());
		dataset.put("task", task.getType());

		super.getResponse().addData(dataset);

	}
}
