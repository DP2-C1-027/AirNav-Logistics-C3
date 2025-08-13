
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
public class AdministratorAircraftUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository repository;

	// AbstractGuiService interfaced ------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		try {
			Integer aircraftId = super.getRequest().getData("id", Integer.class);
			if (aircraftId != null) {
				Aircraft aircraft = this.repository.findAircraftById(aircraftId);
				isAuthorised = aircraft != null && super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Aircraft aircraft = this.repository.findAircraftById(id);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details", "airline");
	}

	@Override
	public void validate(final Aircraft aircraft) {

		Aircraft oldAircraft = this.repository.findAircraftByRegistrationNumber(aircraft.getRegistrationNumber());
		if (oldAircraft != null) {
			boolean idMatch = oldAircraft.getId() == aircraft.getId();
			super.state(idMatch, "registrationNumber", "acme.validation.registrationNumber");
		}

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
