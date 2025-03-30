
package acme.features.technician.maintenanceRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.StatusMaintanenceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianRecordUpdateService extends AbstractGuiService<Technician, MaintanenceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianRecordRepository repository;

	// AbstractService -------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int recordId;
		MaintanenceRecord record;
		Technician tech;

		recordId = super.getRequest().getData("id", int.class);
		record = this.repository.findRecordById(recordId);
		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		status = record != null && record.getDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);

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
	public void bind(final MaintanenceRecord record) {

		super.bindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes");

	}

	@Override
	public void validate(final MaintanenceRecord record) {
		//LAS VALIDACIONES DE ESTO¿?¿?¿?

		//String cod = booking.getLocatorCode();
		//Collection<Booking> codigo = this.repository.findAllBookingLocatorCode(cod).stream().filter(x -> x.getId() != booking.getId()).toList();

		//if (!codigo.isEmpty())
		//	super.state(false, "locatorCode", "customers.booking.error.repeat-code");

	}

	@Override
	public void perform(final MaintanenceRecord record) {
		this.repository.save(record);
	}

	@Override
	public void unbind(final MaintanenceRecord record) {

		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(StatusMaintanenceRecord.class, record.getStatus());
		dataset = super.unbindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes", "draftMode");

		dataset.put("status", choices);
		super.getResponse().addData(dataset);
	}
}
