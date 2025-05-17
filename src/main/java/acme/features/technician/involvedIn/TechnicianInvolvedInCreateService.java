
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
public class TechnicianInvolvedInCreateService extends AbstractGuiService<Technician, InvolvedIn> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvedInRepository repository;


	@Override
	public void authorise() {
		Technician tech;
		boolean status;
		MaintanenceRecord record;
		Task task;
		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		status = super.getRequest().getPrincipal().hasRealm(tech);
		if (super.getRequest().hasData("record")) {
			Integer id;
			try {
				id = super.getRequest().getData("record", Integer.class);
				record = this.repository.findRecordById(id);

				if (!id.equals(Integer.valueOf(0)) && !record.getTechnician().equals(tech))
					status = false;

			} catch (Exception e) {
				status = false;
				record = null;
			}
			status = record != null ? status && record.isDraftMode() : status;
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

		if (super.getRequest().hasData("passenger")) {
			Integer id;
			try {
				id = super.getRequest().getData("passenger", Integer.class);
				task = this.repository.findTaskById(id);

				if (!id.equals(Integer.valueOf(0)) && !task.getTechnician().equals(tech))
					status = false;

			} catch (Exception e) {
				status = false;
			}
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

		if (super.getRequest().hasData("id")) {
			Integer id;
			try {
				id = super.getRequest().getData("id", Integer.class);
				if (!id.equals(Integer.valueOf(0)))
					status = false;

			} catch (Exception e) {
				status = false;

			}
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		InvolvedIn involved;

		involved = new InvolvedIn();

		super.getBuffer().addData(involved);
	}

	@Override
	public void bind(final InvolvedIn involved) {

		super.bindObject(involved, "maintanenceRecord", "task");
	}

	@Override
	public void validate(final InvolvedIn involved) {
		MaintanenceRecord maintanenceRecord;
		Task task;

		maintanenceRecord = involved.getMaintanenceRecord();
		task = involved.getTask();

		super.state(maintanenceRecord != null, "*", "technician.involved-in.create.error.null-record");
		super.state(task != null, "*", "technician.involved-in.create.error.null-task");

		boolean exists = this.repository.existsByRecordAndTask(maintanenceRecord, task);
		super.state(!exists, "*", "technician.involved-in.create.error.duplicate-record-task");

	}

	@Override
	public void perform(final InvolvedIn involved) {
		this.repository.save(involved);
	}

	@Override
	public void unbind(final InvolvedIn involved) {
		Dataset dataset;

		Technician tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		SelectChoices recordChoices;
		SelectChoices taskChoices;

		Collection<Task> tasks = this.repository.findTaskByTechnicianId(tech.getId());

		Collection<MaintanenceRecord> records = this.repository.findNotPublishRecord(tech.getId(), true);

		taskChoices = SelectChoices.from(tasks, "description", involved.getTask());

		recordChoices = SelectChoices.from(records, "maintanenceMoment", involved.getMaintanenceRecord());

		dataset = super.unbindObject(involved, "maintanenceRecord", "task");
		dataset.put("maintanenceRecord", recordChoices);
		dataset.put("task", taskChoices);
		dataset.put("draftMode", true);

		super.addPayload(dataset, involved, "maintanenceRecord", "task");
		super.getResponse().addData(dataset);

	}
}
