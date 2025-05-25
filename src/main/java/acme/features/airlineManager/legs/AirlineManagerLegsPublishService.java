
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
public class AirlineManagerLegsPublishService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegsRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		Leg leg = null;
		AirlineManager manager;
		if (super.getRequest().hasData("id")) {
			Integer legId;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				legId = Integer.valueOf(isInteger);
			else
				legId = null;
			leg = legId == null ? null : this.repository.findLegById(legId);
			manager = leg == null ? null : leg.getFlight().getAirlineManager();
			status = manager == null ? false : super.getRequest().getPrincipal().hasRealm(manager) && leg.isDraftMode();
		} else
			status = false;
		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}
		if (super.getRequest().hasData("duration")) {
			Integer duration;
			String isInteger;
			isInteger = super.getRequest().getData("duration", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				duration = Integer.valueOf(isInteger);
			else
				duration = null;
			status = leg != null && duration != null && duration.equals(leg.getDuration());
		} else
			status = false;
		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}
		if (super.getRequest().hasData("flight")) {
			Integer flightId;
			String isInteger;
			Flight flight;
			isInteger = super.getRequest().getData("flight", String.class);
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				flightId = Integer.valueOf(isInteger);
			else
				flightId = Integer.valueOf(-1);
			flight = flightId == null ? null : this.repository.getFlightById(flightId);
			manager = flight == null ? null : leg.getFlight().getAirlineManager();
			status = manager == null ? flightId != null && flightId.equals(Integer.valueOf(0)) : super.getRequest().getPrincipal().hasRealm(manager);

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
		if (flight == null || aircraft == null)
			return;
		else {
			airlineFlight = flight.getAirline();
			airlineAircraft = aircraft.getAirline();
		}
		String IATAnumber = leg.getFlightNumber();
		Date scheduledDeparture = leg.getScheduledDeparture();
		Date scheduledArrival = leg.getScheduledArrival();
		Airport arrivalAirport = leg.getArrivalAirport();
		Airport departureAirport = leg.getDepartureAirport();
		if (scheduledArrival == null || scheduledDeparture == null || arrivalAirport == null || departureAirport == null || IATAnumber == null)
			return;

		if (IATAnumber == null || airlineFlight == null || !IATAnumber.startsWith(airlineAircraft.getCodigo()))
			super.state(IATAnumber == null || airlineFlight == null, "flightNumber", "airline-manager.error.invalid-flight-number");
		if (scheduledArrival == null || scheduledDeparture == null || !scheduledArrival.after(scheduledDeparture))
			super.state(scheduledArrival == null || scheduledDeparture == null, "scheduledArrival", "airline-manager.error.future-departure");
		if (arrivalAirport == null || departureAirport == null || arrivalAirport.equals(departureAirport))
			super.state(arrivalAirport == null || departureAirport == null, "arrivalAirport", "airline-manager.error.same-airport");
		if (IATAnumber != null && !IATAnumber.isBlank() && this.repository.anyLegByIATAnumber(IATAnumber) && !this.repository.findLegById(leg.getId()).getFlightNumber().equals(IATAnumber))
			super.state(false, "flightNumber", "airline-manager.error.duplicated-code");

		Collection<Leg> publishedLegs;
		Collection<Leg> aircraftPublishedLegs;

		publishedLegs = this.repository.findPublishedLegsByFlightId(flight.getId());
		aircraftPublishedLegs = this.repository.findPublishedLegsByAircraftId(aircraft.getId());

		boolean valid;

		if (!publishedLegs.isEmpty()) {
			valid = publishedLegs.stream().anyMatch((l) -> !(scheduledDeparture.after(l.getScheduledArrival()) || scheduledArrival.before(l.getScheduledDeparture())));
			super.state(!valid, "*", "airline-manager.error.overlappedLegs");
			valid = publishedLegs.stream().anyMatch((l) -> leg.getArrivalAirport().equals(l.getDepartureAirport()) && leg.getScheduledDeparture().after(l.getScheduledDeparture()));
			super.state(!valid, "*", "airline-manager.error.airportLoopArrival");
			valid = publishedLegs.stream().anyMatch((l) -> leg.getDepartureAirport().equals(l.getArrivalAirport()) && leg.getScheduledDeparture().before(l.getScheduledDeparture()));
			super.state(!valid, "*", "airline-manager.error.airportLoopDeparture");
			valid = publishedLegs.stream().anyMatch((l) -> leg.getDepartureAirport().equals(l.getDepartureAirport()));
			super.state(!valid, "*", "airline-manager.error.duplicatedDepartureAirport");
			valid = publishedLegs.stream().anyMatch((l) -> leg.getArrivalAirport().equals(l.getArrivalAirport()));
			super.state(!valid, "*", "airline-manager.error.duplicatedArrivalAirport");
		}

		if (!aircraftPublishedLegs.isEmpty()) {
			valid = aircraftPublishedLegs.stream().anyMatch((l) -> !(scheduledDeparture.after(l.getScheduledArrival()) || scheduledArrival.before(l.getScheduledDeparture())));
			super.state(!valid, "aircraft", "airline-manager.error.aircraft-in-use.message");
		}

	}

	@Override
	public void perform(final Leg leg) {
		assert leg != null;
		leg.setDraftMode(false);
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
		super.addPayload(dataset, leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "duration", "status", "draftMode", "flight", "arrivalAirport", "departureAirport", "aircraft");

		super.getResponse().addData(dataset);
	}
}
