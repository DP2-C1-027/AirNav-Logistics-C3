
package acme.features.airlineManager.flights;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.flightAssignment.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.features.airlineManager.legs.AirlineManagerLegsRepository;
import acme.features.assistanceAgent.claims.AssistanceAgentClaimRepository;
import acme.features.flightCrewMember.activityLog.FlightCrewMemberActivityLogRepository;
import acme.features.flightCrewMember.flightAssignment.FlightCrewMemberFlightAssignmentRepository;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightDeleteService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository				repository;

	@Autowired
	private AirlineManagerLegsRepository				legRepository;

	@Autowired
	private AssistanceAgentClaimRepository				claimRepository;

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository	assignmentRepository;

	@Autowired
	private FlightCrewMemberActivityLogRepository		activityRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		AirlineManager manager;

		masterId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(masterId);
		manager = flight == null ? null : flight.getAirlineManager();
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

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

		super.bindObject(flight, "tag", "indication", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {
		Collection<Leg> legs;
		Collection<Claim> claims;
		Collection<FlightAssignment> assignments;
		Collection<ActivityLog> activities;

		legs = this.repository.findLegsByFlightId(flight.getId());
		claims = this.claimRepository.findClaimsByFlightId(flight.getId());
		assignments = this.assignmentRepository.findFlightAssignmentsByFlightId(flight.getId());
		activities = this.activityRepository.findActivityLogsByFlightId(flight.getId());

		this.claimRepository.deleteAll(claims);
		this.activityRepository.deleteAll(activities);
		this.assignmentRepository.deleteAll(assignments);
		this.legRepository.deleteAll(legs);
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {

		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "indication", "cost", "description");

		super.getResponse().addData(dataset);
	}

}
