
package acme.features.airlineManager.flights;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightShowService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		if (super.getRequest().hasData("id")) {
			Integer flightId;
			String isInteger;
			AirlineManager manager;
			Flight flight;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (isInteger != null && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				flightId = Integer.valueOf(isInteger);
			else
				flightId = Integer.valueOf(-1);
			flight = flightId == null ? null : this.repository.findFlightById(flightId);
			manager = flight == null ? null : flight.getAirlineManager();
			status = manager == null ? false : super.getRequest().getPrincipal().hasRealm(manager);
		} else
			status = false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {

		Dataset dataset;
		Collection<Airline> airlines;
		SelectChoices choices;
		airlines = this.repository.getAllAirlines();
		choices = SelectChoices.from(airlines, "codigo", flight.getAirline());

		dataset = super.unbindObject(flight, "tag", "indication", "cost", "description", "airline", "draftMode");
		dataset.put("airline", choices.getSelected().getKey());
		dataset.put("airlines", choices);
		super.addPayload(dataset, flight, "tag", "indication", "cost", "description", "airline", "draftMode");
		super.getResponse().addData(dataset);
	}

}
