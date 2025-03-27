
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentListPlannedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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
		Date moment = MomentHelper.getCurrentMoment();
		Collection<FlightAssignment> plannedFlightAssignments = this.repository.findAllPlannedFlightAssignments(moment);

		super.getBuffer().addData(plannedFlightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment plannedFlightAssignments) {
		Dataset dataset = super.unbindObject(plannedFlightAssignments, "duty", "moment", "currentStatus", "remarks", "draftMode");
		super.addPayload(dataset, plannedFlightAssignments, "duty", "moment", "currentStatus", "remarks", "draftMode");
		super.getResponse().addData(dataset);

	}
}
