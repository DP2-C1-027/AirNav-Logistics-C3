
package acme.features.airlineManager.legs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegsListFlightService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegsRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		Flight flight;
		AirlineManager manager;
		if (super.getRequest().hasData("masterId", int.class)) {
			Integer flightId;
			String isInteger;
			isInteger = super.getRequest().getData("masterId", String.class);
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				flightId = Integer.valueOf(isInteger);
			else
				flightId = Integer.valueOf(-1);
			flight = this.repository.getFlightById(flightId);
			manager = flight == null ? null : flight.getAirlineManager();
			status = manager == null ? false : super.getRequest().getPrincipal().hasRealm(manager);

		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Leg> legs;
		Flight flight;
		int flightId;

		flightId = super.getRequest().getData("masterId", int.class);

		flight = this.repository.getFlightById(flightId);
		legs = this.repository.findLegsByFlightId(flightId);

		super.getBuffer().addData(legs);
		super.getResponse().addGlobal("flightDraftMode", flight.isDraftMode());
		super.getResponse().addGlobal("flightId", flightId);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");
		dataset.put("departureAirport", leg.getDepartureAirport().getCodigo());
		dataset.put("arrivalAirport", leg.getArrivalAirport().getCodigo());
		dataset.put("flight", leg.getFlight().getTag());
		super.addPayload(dataset, leg);

		super.getResponse().addData(dataset);
	}

}
