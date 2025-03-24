
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;

@GuiService
public class AdministratorAircraftListService extends AbstractGuiService<Administrator, Aircraft> {

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
		Collection<Aircraft> aircrafts = this.repository.findAircraftsByAirlineId(airlineId);
		super.getBuffer().addData(aircrafts);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		int airlineId = super.getRequest().getData("airlineId", int.class);
		Dataset dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details", "airline");
		super.getResponse().addGlobal("airlineId", airlineId);
		super.getResponse().addData(dataset);
	}

}
