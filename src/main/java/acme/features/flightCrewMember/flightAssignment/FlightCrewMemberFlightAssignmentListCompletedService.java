
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

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
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> completedFlightAssignments = this.repository.findAllCompletedFlightAssignments(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(completedFlightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment completedFlightAssignments) {
		Dataset dataset = super.unbindObject(completedFlightAssignments, "duty", "moment", "currentStatus", "remarks", "draftMode", "leg");
		dataset.put("leg", completedFlightAssignments.getLeg().getFlightNumber());

		super.addPayload(dataset, completedFlightAssignments, "duty", "moment", "currentStatus", "remarks", "draftMode", "leg");
		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("showCreate", true);

	}
}
