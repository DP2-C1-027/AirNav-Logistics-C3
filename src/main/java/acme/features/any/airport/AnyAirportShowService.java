
package acme.features.any.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;

@GuiService
public class AnyAirportShowService extends AbstractGuiService<Any, Airport> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyAirportRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isAuthorised = false;

		if (super.getRequest().getMethod().equals("GET") && super.getRequest().getData("id", Integer.class) != null) {
			Integer airportId = super.getRequest().getData("id", Integer.class);
			if (airportId != null) {
				Airport airport = this.repository.findAirport(airportId);
				isAuthorised = airport != null;
			}

		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Airport airport = this.repository.findAirport(id);

		super.getBuffer().addData(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset = super.unbindObject(airport, "name", "codigo", "operationalScope", "city", "country", "website", "email", "phoneNumber");

		super.getResponse().addData(dataset);
	}
}
