
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

		try {
			Integer airportId = super.getRequest().getData("id", Integer.class);
			if (airportId != null) {
				Airport airport = this.repository.findAirport(airportId);
				isAuthorised = airport != null;
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
		Airport airport = this.repository.findAirport(id);

		super.getBuffer().addData(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		assert airport != null;

		Dataset dataset = super.unbindObject(airport, "name", "codigo", "operationalScope", "city", "country", "website", "email", "address", "phoneNumber");

		super.getResponse().addData(dataset);
	}
}
