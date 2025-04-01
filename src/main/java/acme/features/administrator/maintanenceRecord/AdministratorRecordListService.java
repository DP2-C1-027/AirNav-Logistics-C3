
package acme.features.administrator.maintanenceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintanenceRecords.MaintanenceRecord;

@GuiService
public class AdministratorRecordListService extends AbstractGuiService<Administrator, MaintanenceRecord> {
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
		Collection<MaintanenceRecord> record;

		record = this.repository.findAllPublishedRecord();

		super.getBuffer().addData(record);
	}

	@Override
	public void unbind(final MaintanenceRecord record) {
		Dataset dataset;

		dataset = super.unbindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes");
		super.getResponse().addData(dataset);
	}
}
