
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.StatusMaintanenceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianRecordServiceShow extends AbstractGuiService<Technician, MaintanenceRecord> {

	@Autowired
	private TechnicianRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int recordId;
		MaintanenceRecord record;
		Aircraft a;
		Technician tech;

		recordId = super.getRequest().getData("id", int.class);
		record = this.repository.findRecordById(recordId);
		//tendria que encontrar el aircraft que tiene asociado para comprobar que existe

		a = this.repository.findAircraftByRecordId(recordId);
		tech = record == null ? null : record.getTechnician();
		status = super.getRequest().getPrincipal().hasRealm(tech) || a != null;

		super.getResponse().setAuthorised(status);
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
		SelectChoices choices;
		SelectChoices aircraftChoices;

		Collection<Aircraft> aircrafts;
		aircrafts = this.repository.getAllAircraft();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", record.getAircraft());

		choices = SelectChoices.from(StatusMaintanenceRecord.class, record.getStatus());

		dataset = super.unbindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes", "draftMode");
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("status", choices);
		// Derived attributes --------------------
		//no tengo
		super.getResponse().addData(dataset);
	}

}
