
package acme.features.administrator.aircraft;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.Status;

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
		Aircraft aircraft = new Aircraft();
		aircraft.setModel("");
		aircraft.setRegistrationNumber("");
		aircraft.setCapacity(0);
		aircraft.setCargoWeight(0);
		aircraft.setStatus(Status.ACTIVE_SERVICE);
		aircraft.setDetails("");
		aircraft.setAirline(null);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details", "airline");
	}

	@Override
	public void validate(final Aircraft aircraft) {
		String registrationNumber = super.getRequest().getData("registrationNumber", String.class);
		boolean registrationNumberMatch = this.repository.findAllAircrafts().stream().noneMatch(x -> x.getRegistrationNumber().equals(registrationNumber));
		super.state(registrationNumberMatch, "registrationNumber", "acme.validation.registrationNumber");

		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details", "airline");

		SelectChoices statusChoices = SelectChoices.from(Status.class, aircraft.getStatus());
		dataset.put("statusChoices", statusChoices);

		SelectChoices airlinesChoices = SelectChoices.from(this.repository.findAllAirlines(), "name", aircraft.getAirline());
		dataset.put("airlinesChoices", airlinesChoices);

		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}
}
