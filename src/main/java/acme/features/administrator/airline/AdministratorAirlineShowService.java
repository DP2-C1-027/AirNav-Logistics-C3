
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineType;

@GuiService
public class AdministratorAirlineShowService extends AbstractGuiService<Administrator, Airline> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(Administrator.class)) {
			Integer airlineId = super.getRequest().getData("id", Integer.class);

			if (super.getRequest().getMethod().equals("GET") && airlineId != null) {
				Airline airline = this.repository.findAirlineById(airlineId);
				isAuthorised = airline != null;
			}
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Airline airline = this.repository.findAirlineById(id);

		super.getBuffer().addData(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset = super.unbindObject(airline, "name", "codigo", "website", "type", "foundationMoment", "email", "phoneNumber");

		SelectChoices choices = SelectChoices.from(AirlineType.class, airline.getType());
		dataset.put("confirmation", false);
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}
}
