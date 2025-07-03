
package acme.features.any.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.flightcrewmember.FlightCrewMember;

@GuiService
public class AnyFlightAssignmentShowService extends AbstractGuiService<Any, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getMethod().equals("GET") && super.getRequest().getData("id", Integer.class) != null) {
			Integer flightAssignmentId = super.getRequest().getData("id", Integer.class);
			if (flightAssignmentId != null) {
				FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
				isAuthorised = flightAssignment != null && !flightAssignment.getDraftMode() && !flightAssignment.getLeg().isDraftMode();
			}
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment completedFlightAssignment) {
		Dataset dataset = super.unbindObject(completedFlightAssignment, "duty", "moment", "currentStatus", "remarks", "flightCrewMember", "leg", "draftMode");

		FlightCrewMember flightcrewMember = completedFlightAssignment.getFlightCrewMember();
		Leg leg = completedFlightAssignment.getLeg();

		// Flight Crew Member details
		dataset.put("flightCrewMember", flightcrewMember.getIdentity().getFullName());
		dataset.put("codigo", flightcrewMember.getCodigo());
		dataset.put("phoneNumber", flightcrewMember.getPhoneNumber());
		dataset.put("languageSkills", flightcrewMember.getLanguageSkills());
		dataset.put("availabilityStatus", flightcrewMember.getAvailabilityStatus());
		dataset.put("salary", flightcrewMember.getSalary());
		dataset.put("yearsOfExperience", flightcrewMember.getYearsOfExperience());
		dataset.put("airline", flightcrewMember.getAirline().getName());

		// Leg details
		dataset.put("flightNumber", leg.getFlightNumber());
		dataset.put("scheduledDeparture", leg.getScheduledDeparture());
		dataset.put("scheduledArrival", leg.getScheduledArrival());
		dataset.put("status", leg.getStatus());
		dataset.put("duration", leg.getDuration());
		dataset.put("departureAirport", leg.getDepartureAirport().getName());
		dataset.put("arrivalAirport", leg.getArrivalAirport().getName());
		dataset.put("aircraft", leg.getAircraft().getRegistrationNumber());
		dataset.put("flight", leg.getFlight().getTag());
		dataset.put("legAirline", leg.getAircraft().getAirline().getName());

		super.getResponse().addData(dataset);
	}
}
