
package acme.features.technician.maintenanceRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenenceRecordServiceShow extends AbstractGuiService<Technician, MaintanenceRecord> {

	@Autowired
	private TechnicianMaintenenceRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		MaintanenceRecord record;

		id = super.getRequest().getData("id", int.class);
		record = this.repository.findRecordById(id);
		//status = flight != null && !flight.isDraftMode();

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		MaintanenceRecord record;
		int id;

		id = super.getRequest().getData("id", int.class);
		record = this.repository.findRecordById(id);

		super.getBuffer().addData(record);
	}

	@Override
	public void unbind(final MaintanenceRecord record) {

		Dataset dataset;

		dataset = super.unbindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes");

		// Derived attributes --------------------
		super.getResponse().addData(dataset);
	}

}
