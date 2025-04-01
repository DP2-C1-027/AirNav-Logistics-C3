
package acme.features.administrator.maintanenceRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintanenceRecords.MaintanenceRecord;

@GuiService
public class AdministratorRecordShowService extends AbstractGuiService<Administrator, MaintanenceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		MaintanenceRecord record;
		int id;

		id = super.getRequest().getData("id", int.class);
		record = this.repository.findById(id);

		super.getBuffer().addData(record);
	}

	@Override
	public void unbind(final MaintanenceRecord record) {

		Dataset dataset;

		dataset = super.unbindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes", "draftMode");

		super.getResponse().addData(dataset);
	}
}
