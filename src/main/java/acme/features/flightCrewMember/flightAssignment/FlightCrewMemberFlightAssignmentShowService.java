
package acme.features.flightCrewMember.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.CurrentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
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

		SelectChoices dutyChoices = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);

		SelectChoices statusChoices = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());
		dataset.put("statusChoices", statusChoices);

		FlightCrewMember flightCrewMember = (FlightCrewMember) dataset.get("flightCrewMember");
		dataset.put("flightCrewMember", flightCrewMember.getCode());

		Leg leg = (Leg) dataset.get("leg");
		dataset.put("leg", leg.getFlightNumber());

		super.getResponse().addData(dataset);
	}
}
