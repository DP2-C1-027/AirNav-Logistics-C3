
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
public class TechnicianInvolvedInDeleteService extends AbstractGuiService<Technician, InvolvedIn> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvedInRepository repository;

	// AbstractGuiService interface -------------------------------------------


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
			status = tech == null ? recordId != null && recordId.equals(Integer.valueOf(0)) : super.getRequest().getPrincipal().hasRealm(tech);

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
			status = tech == null ? id != null && id.equals(Integer.valueOf(0)) : super.getRequest().getPrincipal().hasRealm(tech);
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
			status = tech != null && super.getRequest().getPrincipal().hasRealm(tech) && involvedIn.getMaintanenceRecord().isDraftMode();

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
		//Mirar esta validacion...
		InvolvedIn involvedInDB = this.repository.findInvolvedIn(involved.getId());
		super.state(involvedInDB.getMaintanenceRecord().isDraftMode(), "*", "customers.form.error.draft-mode");
		;
	}

	@Override
	public void perform(final InvolvedIn involved) {

		this.repository.delete(involved);

	}

	@Override
	public void unbind(final InvolvedIn involved) {
		Dataset dataset;
		Technician tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		Collection<Task> task = this.repository.findTaskByTechnicianId(tech.getId());

		Collection<MaintanenceRecord> records;

		records = this.repository.findNotPublishRecord(tech.getId(), true);

		SelectChoices taskChoices;
		SelectChoices recordChoices;

		taskChoices = SelectChoices.from(task, "description", involved.getTask());

		recordChoices = SelectChoices.from(records, "maintanenceMoment", involved.getMaintanenceRecord());

		dataset = super.unbindObject(involved, "maintanenceRecord", "task");
		dataset.put("maintanenceRecord", recordChoices);
		dataset.put("task", taskChoices);
		//IsDraftMode()
		dataset.put("draftMode", involved.getMaintanenceRecord().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
