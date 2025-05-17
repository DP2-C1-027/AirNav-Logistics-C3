
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
public class TechnicianInvolvedInUpdateService extends AbstractGuiService<Technician, InvolvedIn> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvedInRepository repository;


	@Override
	public void authorise() {
		Technician tech;
		boolean status = true;
		MaintanenceRecord record;
		Task task;
		if (super.getRequest().hasData("id", int.class)) {
			Integer involvedInId;
			try {
				involvedInId = super.getRequest().getData("id", Integer.class);
			} catch (Exception e) {
				involvedInId = null;
			}
			//el id es 548
			task = involvedInId != null ? this.repository.findOneTaskByInvolvedIn(involvedInId) : null;
			record = involvedInId != null ? this.repository.findOneRecordByInvolvedIn(involvedInId) : null;
			tech = record != null ? record.getTechnician() : null;
			status = tech == null ? false : record != null && task != null && record.isDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);
			if (super.getRequest().hasData("task")) {
				Integer id;
				try {
					id = super.getRequest().getData("task", Integer.class);
					task = this.repository.findTaskById(id);

					if (!id.equals(Integer.valueOf(0)) && !task.getTechnician().equals(tech))
						status = false;

				} catch (Exception e) {
					status = false;
				}
			}

			if (super.getRequest().hasData("maintanenceRecord")) {
				Integer id;
				try {
					id = super.getRequest().getData("maintanenceRecord", Integer.class);
					record = this.repository.findRecordById(id);

					if (!id.equals(Integer.valueOf(0)) && !record.getTechnician().equals(tech))
						status = false;

				} catch (Exception e) {
					status = false;
					record = null;
				}
				status = record != null ? status && record.isDraftMode() : status;
				//mira que esté publicado o no
			}

		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		InvolvedIn involved;
		int id;
		id = super.getRequest().getData("id", int.class);
		involved = this.repository.findInvolvedIn(id);

		super.getBuffer().addData(involved);
	}

	@Override
	public void bind(final InvolvedIn involved) {

		super.bindObject(involved, "maintanenceRecord", "task");
	}

	@Override
	public void validate(final InvolvedIn involved) {
		MaintanenceRecord record = involved.getMaintanenceRecord();
		Task task = involved.getTask();
		InvolvedIn currentInvolvedIn;
		currentInvolvedIn = this.repository.findInvolvedIn(involved.getId());
		super.state(record != null, "*", "technician.involved-in.create.error.null-record");
		super.state(task != null, "*", "technician.involved-in.create.error.null-task");
		Collection<InvolvedIn> exists = this.repository.findByRecordAndTask(record, task);
		boolean exists1 = exists.contains(currentInvolvedIn) ? exists.size() > 1 : exists.size() > 0;
		super.state(!exists1, "*", "technician.involved-in.create.error.duplicate-record-task");
	}

	@Override
	public void perform(final InvolvedIn involved) {
		this.repository.save(involved);
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
