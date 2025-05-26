
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
public class AirlineManagerLegsCreateService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegsRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		AirlineManager manager;
		Flight flight;
		Airport airport;
		Aircraft aircraft;
		Integer flightId;
		Integer airportId;
		Integer aircraftId;

		if (super.getRequest().hasData("flightId")) {
			String isInteger;
			isInteger = super.getRequest().getData("flightId", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				flightId = Integer.valueOf(isInteger);
			else
				flightId = Integer.valueOf(-1);
			flight = this.repository.getFlightById(flightId);
			manager = flight == null ? null : flight.getAirlineManager();
			status = manager == null ? false : super.getRequest().getPrincipal().hasRealm(manager) && flight.isDraftMode();
		}
		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}
		if (super.getRequest().hasData("id")) {
			Integer legId;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				legId = Integer.valueOf(isInteger);
			else
				legId = Integer.valueOf(-1);
			if (!legId.equals(Integer.valueOf(0)))
				status = false;
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;
		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}
		if (super.getRequest().hasData("duration")) {
			Integer duration;
			String isInteger;
			isInteger = super.getRequest().getData("duration", String.class);
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				duration = Integer.valueOf(isInteger);
			else
				duration = Integer.valueOf(-1);
			if (!duration.equals(0))
				status = false;
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;
		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}

		if (super.getRequest().hasData("flight")) {
			String isInteger;
			isInteger = super.getRequest().getData("flight", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				flightId = Integer.valueOf(isInteger);
			else
				flightId = Integer.valueOf(-1);
			flight = this.repository.getFlightById(flightId);
			manager = flight == null ? null : flight.getAirlineManager();
			status = manager == null ? flightId.equals(Integer.valueOf(0)) : super.getRequest().getPrincipal().hasRealm(manager) && flight.isDraftMode();
		} else if (super.getRequest().getMethod().equals("POST"))
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
		} else if (super.getRequest().getMethod().equals("POST"))
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
		} else if (super.getRequest().getMethod().equals("POST"))
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
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;
		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}

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
			super.state(false, "flightNumber", "airline-manager.error.duplicated-code");
	}

	@Override
	public void perform(final Leg leg) {
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
		dataset.put("status", choicesStatus.getSelected().getKey());
		dataset.put("statuses", choicesStatus);
		super.addPayload(dataset, leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode", "flight", "arrivalAirport", "departureAirport", "aircraft");

		super.getResponse().addData(dataset);
	}

}
