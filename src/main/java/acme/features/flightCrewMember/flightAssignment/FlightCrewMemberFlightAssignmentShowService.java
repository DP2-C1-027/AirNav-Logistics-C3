
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.CurrentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "flightCrewMember", "leg", "draftMode");

		// Duty choices
		SelectChoices dutyChoices = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);

		// Status choices
		SelectChoices statusChoices = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());
		dataset.put("statusChoices", statusChoices);

		// Flight Crew Member choices
		Collection<FlightCrewMember> flightCrewMembers = this.repository.findAllflightCrewMemberFromAirline(flightAssignment.getFlightCrewMember().getAirline().getId());
		SelectChoices flightCrewMemberChoices = SelectChoices.from(flightCrewMembers, "identity.fullName", flightAssignment.getFlightCrewMember());
		dataset.put("flightCrewMemberChoices", flightCrewMemberChoices);
		dataset.put("readOnlyCrewMember", true);

		// Leg choices
		SelectChoices legChoices = SelectChoices.from(this.repository.findAllLegs(), "flightNumber", flightAssignment.getLeg());
		dataset.put("legChoices", legChoices);

		super.getResponse().addData(dataset);
	}
}
