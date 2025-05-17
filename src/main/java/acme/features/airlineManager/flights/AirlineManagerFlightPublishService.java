
package acme.features.airlineManager.flights;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightPublishService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		Integer masterId;
		Flight flight;
		AirlineManager manager;
		if (super.getRequest().hasData("id")) {
			try {
				masterId = super.getRequest().getData("id", Integer.class);
			} catch (Exception e) {
				masterId = null;
			}
			flight = masterId == null ? null : this.repository.findFlightById(masterId);
			manager = flight == null ? null : flight.getAirlineManager();
			status = manager == null ? false : super.getRequest().getPrincipal().hasRealm(manager) && flight.isDraftMode();
		} else
			status = false;
		if (super.getRequest().hasData("indication"))
			try {
				if (super.getRequest().getData("indication", Boolean.class) == null)
					status = false;
			} catch (Exception e) {
				status = false;
			}
		else
			status = false;
		if (super.getRequest().hasData("airline")) {
			Integer airlineId;
			try {
				airlineId = super.getRequest().getData("airline", Integer.class);
			} catch (Exception e) {
				status = false;
				airlineId = Integer.valueOf(-1);
			}
			if (airlineId == null || !airlineId.equals(Integer.valueOf(0)) && this.repository.getAirlineById(airlineId) == null)
				status = false;
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
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "indication", "cost", "description", "airline");
	}

	@Override
	public void validate(final Flight flight) {

		Collection<Leg> legs;
		boolean confirmation;

		legs = this.repository.findLegsByFlightId(flight.getId());
		confirmation = !legs.stream().anyMatch((leg) -> !leg.isDraftMode());

		super.state(confirmation, "*", "airlineManager.flight.error.unpublishedLegs.message");
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
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
