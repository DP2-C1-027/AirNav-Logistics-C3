
package acme.features.any.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;

@GuiService
public class AnyFlightAssignmentShowService extends AbstractGuiService<Any, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getData("id", Integer.class) != null) {
			Integer flightAssignmentId = super.getRequest().getData("id", Integer.class);
			if (flightAssignmentId != null) {
				FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
				isAuthorised = flightAssignment != null && !flightAssignment.getDraftMode();
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

		dataset.put("flightCrewMember", completedFlightAssignment.getFlightCrewMember().getIdentity().getFullName());

		// Leg choices
		dataset.put("leg", completedFlightAssignment.getLeg().getFlightNumber());

		super.getResponse().addData(dataset);
	}
}
