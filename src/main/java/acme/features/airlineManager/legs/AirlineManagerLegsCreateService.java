
package acme.features.airlineManager.legs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegsCreateService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegsRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		Integer masterId;
		AirlineManager manager;
		Flight flight;
		Integer flightId;

		if (super.getRequest().hasData("flightId")) {
			try {
				flightId = super.getRequest().getData("flightId", Integer.class);
			} catch (Exception e) {
				flightId = null;
			}
			flight = flightId == null ? null : this.repository.getFlightById(flightId);
			manager = flight == null ? null : flight.getAirlineManager();
			status = manager == null ? false : super.getRequest().getPrincipal().hasRealm(manager);
		}
		if (super.getRequest().hasData("id")) {
			try {
				masterId = super.getRequest().getData("id", Integer.class);
			} catch (Exception e) {
				masterId = Integer.valueOf(-1);
			}
			if (masterId == null || !masterId.equals(Integer.valueOf(0)))
				status = false;
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;
		if (super.getRequest().hasData("duration")) {
			Integer duration;
			try {
				duration = super.getRequest().getData("duration", Integer.class);
				if (!duration.equals(Integer.valueOf(0)))
					status = false;
			} catch (Exception e) {
				status = false;
			}
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

		super.getRequest().getDataEntries().forEach((e) -> System.out.println(e.getKey().concat(": ").concat(e.getValue().toString())));
		if (super.getRequest().hasData("flight")) {

			try {
				flightId = super.getRequest().getData("flight", Integer.class);
			} catch (Exception e) {
				flightId = Integer.valueOf(-1);
			}
			flight = flightId == null ? null : this.repository.getFlightById(flightId);
			manager = flight == null ? null : flight.getAirlineManager();
			status = manager == null ? flightId != null && flightId.equals(Integer.valueOf(0)) && status : super.getRequest().getPrincipal().hasRealm(manager) && status;
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;

		leg = new Leg();
		leg.setDraftMode(true);

		if (super.getRequest().hasData("flightId"))
			leg.setFlight(this.repository.getFlightById(super.getRequest().getData("flightId", int.class)));
		leg.setDuration(Integer.valueOf(0));
		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "duration", "status", "departureAirport", "arrivalAirport", "aircraft", "flight");
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		assert leg != null;
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		int airlineManagerId;
		Collection<Flight> flights;
		Collection<Airport> airports;
		Collection<Aircraft> aircrafts;
		SelectChoices choicesFlight;
		SelectChoices choicesArrivalAirports;
		SelectChoices choicesDepartureAirports;
		SelectChoices choicesAircraft;
		SelectChoices choicesStatus;
		Dataset dataset;

		airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flights = this.repository.findFlightsByAirlineManagerId(airlineManagerId);
		airports = this.repository.getAllAirports();
		aircrafts = this.repository.getAllAircrafts();
		choicesFlight = SelectChoices.from(flights, "tag", leg.getFlight());
		choicesArrivalAirports = SelectChoices.from(airports, "codigo", leg.getArrivalAirport());
		choicesDepartureAirports = SelectChoices.from(airports, "codigo", leg.getDepartureAirport());
		choicesAircraft = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
		choicesStatus = SelectChoices.from(LegStatus.class, leg.getStatus());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "duration", "status", "draftMode", "flight", "arrivalAirport", "departureAirport", "aircraft");
		dataset.put("flight", choicesFlight.getSelected().getKey());
		dataset.put("flights", choicesFlight);
		dataset.put("arrivalAirport", choicesArrivalAirports.getSelected().getKey());
		dataset.put("arrivalAirports", choicesArrivalAirports);
		dataset.put("departureAirport", choicesDepartureAirports.getSelected().getKey());
		dataset.put("departureAirports", choicesDepartureAirports);
		dataset.put("aircraft", choicesAircraft.getSelected().getKey());
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("status", choicesStatus.getSelected().getKey());
		dataset.put("statuses", choicesStatus);
		super.addPayload(dataset, leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode", "flight", "arrivalAirport", "departureAirport", "aircraft");

		super.getResponse().addData(dataset);
	}

}
