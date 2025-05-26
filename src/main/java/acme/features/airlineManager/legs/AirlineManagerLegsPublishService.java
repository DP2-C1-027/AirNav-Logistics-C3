
package acme.features.airlineManager.legs;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
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
		Aircraft aircraft;
		Airport airport;
		Flight flight;
		Integer duration;
		Integer legId;
		Integer flightId;
		Integer airportId;
		Integer aircraftId;

		if (super.getRequest().hasData("id")) {
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				legId = Integer.valueOf(isInteger);
			else
				legId = Integer.valueOf(-1);
			leg = this.repository.findLegById(legId);
			manager = leg == null ? null : leg.getFlight().getAirlineManager();
			status = manager != null && super.getRequest().getPrincipal().hasRealm(manager) && leg.isDraftMode();
		} else
			status = false;
		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}
		if (super.getRequest().hasData("duration")) {
			String isInteger;
			isInteger = super.getRequest().getData("duration", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				duration = Integer.valueOf(isInteger);
			else
				duration = Integer.valueOf(-1);
			status = duration.equals(leg.getDuration());
		} else
			status = false;
		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}
		if (super.getRequest().hasData("flight")) {
			String isInteger;
			isInteger = super.getRequest().getData("flight", String.class);
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				flightId = Integer.valueOf(isInteger);
			else
				flightId = Integer.valueOf(-1);
			flight = this.repository.getFlightById(flightId);
			manager = flight == null ? null : flight.getAirlineManager();
			status = manager == null ? flightId.equals(Integer.valueOf(0)) : super.getRequest().getPrincipal().hasRealm(manager) && flight.isDraftMode();
		} else
			status = false;
		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}

		if (super.getRequest().hasData("departureAirport")) {
			String isInteger;
			isInteger = super.getRequest().getData("departureAirport", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				airportId = Integer.valueOf(isInteger);
			else
				airportId = Integer.valueOf(-1);
			airport = this.repository.findAirportById(airportId);
			status = airport != null || airportId.equals(Integer.valueOf(0));
		} else
			status = false;
		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}

		if (super.getRequest().hasData("arrivalAirport")) {
			String isInteger;
			isInteger = super.getRequest().getData("arrivalAirport", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				airportId = Integer.valueOf(isInteger);
			else
				airportId = Integer.valueOf(-1);
			airport = this.repository.findAirportById(airportId);
			status = airport != null || airportId.equals(Integer.valueOf(0));
		} else
			status = false;
		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}

		if (super.getRequest().hasData("aircraft")) {
			String isInteger;
			isInteger = super.getRequest().getData("aircraft", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				aircraftId = Integer.valueOf(isInteger);
			else
				aircraftId = Integer.valueOf(-1);
			aircraft = this.repository.findAircraftById(aircraftId);
			status = aircraft != null || aircraftId.equals(Integer.valueOf(0));
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

		if (flight == null || aircraft == null)
			return;

		airlineAircraft = aircraft.getAirline();

		String IATAnumber = leg.getFlightNumber();
		Date scheduledDeparture = leg.getScheduledDeparture();
		Date scheduledArrival = leg.getScheduledArrival();
		Airport arrivalAirport = leg.getArrivalAirport();
		Airport departureAirport = leg.getDepartureAirport();

		if (scheduledArrival == null || scheduledDeparture == null || arrivalAirport == null || departureAirport == null)
			return;

		if (!IATAnumber.startsWith(airlineAircraft.getCodigo()))
			super.state(false, "flightNumber", "airline-manager.error.invalid-flight-number");
		if (!scheduledArrival.after(scheduledDeparture))
			super.state(false, "scheduledArrival", "airline-manager.error.future-departure");
		if (!scheduledDeparture.after(MomentHelper.getCurrentMoment()))
			super.state(false, "scheduledDeparture", "airline-manager.error.leg-in-the-past");
		if (arrivalAirport.equals(departureAirport))
			super.state(false, "arrivalAirport", "airline-manager.error.same-airport");
		if (this.repository.anyLegByIATAnumber(IATAnumber))
			super.state(this.repository.findLegById(leg.getId()).getFlightNumber().equals(IATAnumber), "flightNumber", "airline-manager.error.duplicated-code");

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
		leg.setDraftMode(false);
		Integer duration = Integer.valueOf((int) (leg.getScheduledArrival().getTime() - leg.getScheduledDeparture().getTime()) / (1000 * 60 * 60));
		leg.setDuration(duration);
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
		flights = this.repository.findUnpublishedFlightsByAirlineManagerId(airlineManagerId);
		airports = this.repository.getAllAirports();
		aircrafts = this.repository.getAllAircrafts();
		choicesFlight = SelectChoices.from(flights, "tag", leg.getFlight());
		choicesArrivalAirports = SelectChoices.from(airports, "codigo", leg.getArrivalAirport());
		choicesDepartureAirports = SelectChoices.from(airports, "codigo", leg.getDepartureAirport());
		choicesAircraft = SelectChoices.from(aircrafts, "legString", leg.getAircraft());
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
