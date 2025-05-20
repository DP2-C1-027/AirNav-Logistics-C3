
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
public class AirlineManagerFlightUpdateService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository repository;

	// AbstractService<AirlineManager, Flight> -------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		Flight flight;
		AirlineManager manager;
		if (super.getRequest().hasData("id")) {
			Integer flightId;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (isInteger != null && isInteger.chars().anyMatch((e) -> e > 47 && e < 58))
				flightId = Integer.valueOf(isInteger);
			else
				flightId = Integer.valueOf(-1);
			flight = flightId == null ? null : this.repository.findFlightById(flightId);
			manager = flight == null ? null : flight.getAirlineManager();
			status = manager == null ? false : super.getRequest().getPrincipal().hasRealm(manager) && flight.isDraftMode();
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

		if (super.getRequest().hasData("indication")) {
			String isBoolean;
			isBoolean = super.getRequest().getData("indication", String.class);
			if (isBoolean == null || !(isBoolean.equals("true") || isBoolean.equals("false")))
				status = false;
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

		if (super.getRequest().hasData("airline")) {
			Integer airlineId;
			String isInteger;
			isInteger = super.getRequest().getData("airline", String.class);
			if (isInteger != null && isInteger.chars().anyMatch((e) -> e > 47 && e < 58))
				airlineId = Integer.valueOf(isInteger);
			else
				airlineId = Integer.valueOf(-1);
			if (airlineId == null || !airlineId.equals(0) && this.repository.getAirlineById(airlineId) == null)
				status = false;
		} else if (super.getRequest().getMethod().equals("POST"))
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
	public void bind(final Flight flight) {

		super.bindObject(flight, "tag", "indication", "cost", "description", "airline");
	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {
		assert flight != null;

		this.repository.save(flight);
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
