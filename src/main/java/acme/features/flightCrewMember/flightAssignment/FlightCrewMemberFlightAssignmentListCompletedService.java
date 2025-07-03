
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightcrewmember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentListCompletedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		// Only flight crew members can access this list
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		Collection<FlightAssignment> completedFlightAssignments = this.repository.findAllCompletedFlightAssignments(MomentHelper.getCurrentMoment(), flightCrewMember.getId());

		super.getBuffer().addData(completedFlightAssignments);

		// Disable the create button in the list of completed flight assignments
		super.getResponse().addGlobal("showCreate", false);
	}

	@Override
	public void unbind(final FlightAssignment completedFlightAssignments) {
		Dataset dataset = super.unbindObject(completedFlightAssignments, "duty", "moment", "currentStatus", "remarks", "draftMode", "leg");

		dataset.put("leg", completedFlightAssignments.getLeg().getFlightNumber());

		super.addPayload(dataset, completedFlightAssignments, "duty", "moment", "currentStatus", "remarks", "draftMode", "leg");
		super.getResponse().addData(dataset);
	}
}
