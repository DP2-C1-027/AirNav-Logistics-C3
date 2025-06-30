
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

		boolean status = true;
		Task task;
		Technician tech;

		if (super.getRequest().hasData("maintanenceRecord")) {
			Integer recordId;
			String isInteger;
			isInteger = super.getRequest().getData("maintanenceRecord", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				recordId = Integer.valueOf(isInteger);
			else
				recordId = Integer.valueOf(-1);

			MaintanenceRecord record = this.repository.findRecordById(recordId);
			tech = record != null ? record.getTechnician() : null;
			status = tech == null ? recordId.equals(Integer.valueOf(0)) : super.getRequest().getPrincipal().hasRealm(tech) && record.isDraftMode();

		} else
			status = false;

		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}

		if (super.getRequest().hasData("task")) {
			Integer id;
			String isInteger2;
			isInteger2 = super.getRequest().getData("task", String.class);
			if (!isInteger2.isBlank() && isInteger2.chars().allMatch((e) -> e > 47 && e < 58))
				id = Integer.valueOf(isInteger2);
			else
				id = Integer.valueOf(-1);

			task = this.repository.findTaskById(id);
			tech = task == null ? null : task.getTechnician();
			status = tech == null ? id.equals(Integer.valueOf(0)) : super.getRequest().getPrincipal().hasRealm(tech);
		} else
			status = false;

		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}

		if (super.getRequest().hasData("id")) {
			Integer id;
			String isInteger;
			InvolvedIn involvedIn;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				id = Integer.valueOf(isInteger);
			else
				id = Integer.valueOf(-1);
			involvedIn = this.repository.findInvolvedIn(id);
			tech = involvedIn == null ? null : involvedIn.getTask().getTechnician();
			status = tech != null && involvedIn.getMaintanenceRecord().isDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);

		} else
			status = false;

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
		InvolvedIn involvedDB = this.repository.findInvolvedIn(involved.getId());
		//tienen que ser todas las tasks de la BD
		Collection<Task> tasks = this.repository.findAllTasks();
		Collection<MaintanenceRecord> records;

		records = this.repository.findNotPublishRecord(tech.getId(), true);
		//solo se pueden editar aquellos que no estén publicados

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
