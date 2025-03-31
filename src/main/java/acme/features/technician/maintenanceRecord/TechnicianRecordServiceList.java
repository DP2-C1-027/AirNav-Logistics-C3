
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianRecordServiceList extends AbstractGuiService<Technician, MaintanenceRecord> {

	@Autowired
	private TechnicianRecordRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		Technician technician;
		boolean status;
		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		status = super.getRequest().getPrincipal().hasRealm(technician);
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<MaintanenceRecord> record;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		record = this.repository.getMyMaintanenceRecords(technicianId);

		super.getBuffer().addData(record);
	}

	@Override
	public void unbind(final MaintanenceRecord record) {
		Dataset dataset;

		dataset = super.unbindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes");
		super.getResponse().addData(dataset);

	}
}
