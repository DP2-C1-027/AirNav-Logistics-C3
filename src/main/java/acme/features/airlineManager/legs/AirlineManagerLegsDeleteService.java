
package acme.features.airlineManager.legs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.claims.Claim;
import acme.entities.flightAssignment.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.features.assistanceAgent.claims.AssistanceAgentClaimRepository;
import acme.features.flightCrewMember.activityLog.FlightCrewMemberActivityLogRepository;
import acme.features.flightCrewMember.flightAssignment.FlightCrewMemberFlightAssignmentRepository;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegsDeleteService extends AbstractGuiService<AirlineManager, Leg> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegsRepository				repository;

	@Autowired
	private AssistanceAgentClaimRepository				claimRepository;

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository	assignmentRepository;

	@Autowired
	private FlightCrewMemberActivityLogRepository		activityRepository;

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
		;
	}

	@Override
	public void perform(final Leg leg) {
		Collection<Claim> claims;
		Collection<FlightAssignment> assignments;
		Collection<ActivityLog> activities;

		assignments = this.assignmentRepository.findFlightAssignmentsByLegId(leg.getId());
		activities = this.activityRepository.findActivityLogsByLegId(leg.getId());
		claims = this.claimRepository.findClaimsByLegId(leg.getId());

		this.claimRepository.deleteAll(claims);
		this.activityRepository.deleteAll(activities);
		this.assignmentRepository.deleteAll(assignments);
		this.repository.delete(leg);
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
