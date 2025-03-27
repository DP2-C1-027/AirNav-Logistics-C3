
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
public class FlightCrewMemberFlightAssignmentListCompletedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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
		Collection<FlightAssignment> completedFlightAssignments = this.repository.findAllCompletedFlightAssignments(moment);

		super.getBuffer().addData(completedFlightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment completedFlightAssignments) {
		Dataset dataset = super.unbindObject(completedFlightAssignments, "duty", "moment", "currentStatus", "remarks", "draftMode");
		super.addPayload(dataset, completedFlightAssignments, "duty", "moment", "currentStatus", "remarks", "draftMode");
		super.getResponse().addData(dataset);

	}
}
