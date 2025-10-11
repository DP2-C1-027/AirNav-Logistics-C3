
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
		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(Administrator.class)) {
			if (super.getRequest().getMethod().equals("GET"))
				isAuthorised = true;

			// Only is allowed to create an aircraft if post method include a valid aircraft.
			if (super.getRequest().getMethod().equals("POST") && super.getRequest().getData("id", Integer.class) != null)
				isAuthorised = super.getRequest().getData("id", Integer.class).equals(0);
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		Aircraft aircraft = new Aircraft();

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details", "airline");
	}

	@Override
	public void validate(final Aircraft aircraft) {
		// Check if the code related with an aircraft is already used by another aircraft
		Aircraft existingAircraft = this.repository.findAircraftCode(aircraft.getRegistrationNumber());
		boolean uniqueAircraft = existingAircraft == null || existingAircraft.equals(aircraft);
		super.state(uniqueAircraft, "registrationNumber", "acme.validation.registrationNumber");

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
		dataset.put("status", statusChoices.getSelected().getKey());

		SelectChoices airlinesChoices = SelectChoices.from(this.repository.findAllAirlines(), "name", aircraft.getAirline());
		dataset.put("airlinesChoices", airlinesChoices);
		dataset.put("airline", airlinesChoices.getSelected().getKey());

		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}
}
