
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
public class FlightCrewMemberFlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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
		int flightAssignmentId = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		super.bindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "flightCrewMember", "leg");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		super.state(flightAssignment.getFlightCrewMember() != null, "flightCrewMember", "acme.validation.flightAssignment.flightcrewmember");
		super.state(flightAssignment.getLeg() != null, "leg", "acme.validation.flightAssignment.leg");
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "draftMode", "flightCrewMember", "leg");

		SelectChoices dutyChoices = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);

		SelectChoices statusChoices = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());
		dataset.put("statusChoices", statusChoices);

		FlightAssignment oldFlightAssignment = this.repository.findFlightAssignmentById(flightAssignment.getId());
		Collection<FlightCrewMember> flightCrewMembers = this.repository.findAllFlightCrewMemberFromAirline(oldFlightAssignment.getFlightCrewMember().getAirline().getId());
		SelectChoices flightCrewMemberChoices = SelectChoices.from(flightCrewMembers, "identity.fullName", oldFlightAssignment.getFlightCrewMember());
		dataset.put("flightCrewMemberChoices", flightCrewMemberChoices);

		SelectChoices legChoices = SelectChoices.from(this.repository.findAllLegs(), "flightNumber", flightAssignment.getLeg());
		dataset.put("legChoices", legChoices);

		super.getResponse().addData(dataset);
	}

}
