
package acme.features.administrator.aircraft;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.Status;
import acme.entities.airline.Airline;

@GuiService
public class AdministratorAircraftCreateService extends AbstractGuiService<Administrator, Aircraft> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int airlineId = super.getRequest().getData("airlineId", int.class);
		Airline airline = this.repository.findAirlineById(airlineId);

		Aircraft aircraft = new Aircraft();
		aircraft.setModel("");
		aircraft.setRegistrationNumber("");
		aircraft.setCapacity(0);
		aircraft.setCargoWeight(0);
		aircraft.setStatus(Status.ACTIVE_SERVICE);
		aircraft.setDetails("");
		aircraft.setAirline(airline);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");

		SelectChoices choices = SelectChoices.from(Status.class, Status.ACTIVE_SERVICE);
		dataset.put("types", choices);
		dataset.put("airlineId", super.getRequest().getData("airlineId", int.class));
		dataset.put("confirmation", false);
		dataset.put("draftMode", aircraft.getAirline().isDraftMode());

		super.getResponse().addData(dataset);
	}
}
