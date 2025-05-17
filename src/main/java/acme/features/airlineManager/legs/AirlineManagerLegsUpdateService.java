
package acme.features.airlineManager.legs;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.Airline;
import acme.entities.airport.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegsUpdateService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegsRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		Integer masterId;
		Leg leg = null;
		AirlineManager manager;
		if (super.getRequest().hasData("id")) {
			try {
				masterId = super.getRequest().getData("id", Integer.class);
			} catch (Exception e) {
				masterId = null;
			}
			leg = masterId == null ? null : this.repository.findLegById(masterId);
			manager = leg == null ? null : leg.getFlight().getAirlineManager();
			status = manager == null ? false : super.getRequest().getPrincipal().hasRealm(manager) && leg.isDraftMode();
		} else
			status = false;

		if (super.getRequest().hasData("duration")) {
			Integer duration;
			try {
				duration = super.getRequest().getData("duration", Integer.class);
				if (leg == null ? true : !duration.equals(leg.getDuration()))
					status = false;
			} catch (Exception e) {
				status = false;
			}
		} else
			status = false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "duration", "status", "departureAirport", "arrivalAirport", "aircraft", "flight");
	}

	@Override
	public void validate(final Leg leg) {
		Aircraft aircraft = leg.getAircraft();
		Flight flight = leg.getFlight();
		Airline airlineAircraft;
		Airline airlineFlight;
		if (flight == null || aircraft == null) {
			airlineFlight = null;
			airlineAircraft = null;
		} else {
			airlineFlight = flight.getAirline();
			airlineAircraft = aircraft.getAirline();
		}
		String IATAnumber = leg.getFlightNumber();
		Date scheduledDeparture = leg.getScheduledDeparture();
		Date scheduledArrival = leg.getScheduledArrival();
		Airport arrivalAirport = leg.getArrivalAirport();
		Airport departureAirport = leg.getDepartureAirport();

		if (airlineFlight == null || airlineAircraft == null || !airlineFlight.equals(airlineAircraft))
			super.state(flight == null || aircraft == null, "aircraft", "airline-manager.error.wrong-airline");
		if (IATAnumber == null || airlineFlight == null || !IATAnumber.startsWith(airlineFlight.getCodigo()))
			super.state(IATAnumber == null || airlineFlight == null, "flightNumber", "airline-manager.error.invalid-flight-number");
		if (scheduledArrival == null || scheduledDeparture == null || !scheduledArrival.after(scheduledDeparture))
			super.state(scheduledArrival == null || scheduledDeparture == null, "scheduledArrival", "airline-manager.error.future-departure");
		if (arrivalAirport == null || departureAirport == null || arrivalAirport.equals(departureAirport))
			super.state(arrivalAirport == null || departureAirport == null, "arrivalAirport", "airline-manager.error.same-airport");
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
		dataset.put("statuses", choicesStatus);
		super.addPayload(dataset, leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode", "flight", "arrivalAirport", "departureAirport", "aircraft");

		super.getResponse().addData(dataset);
	}
}
