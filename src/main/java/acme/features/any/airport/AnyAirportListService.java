
package acme.features.any.airport;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;

@GuiService
public class AnyAirportListService extends AbstractGuiService<Any, Airport> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyAirportRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Airport> airports = this.repository.findAllAirport();

		super.getBuffer().addData(airports);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;

		dataset = super.unbindObject(airport, "name", "codigo", "operationalScope");

		super.getResponse().addData(dataset);

	}
}
