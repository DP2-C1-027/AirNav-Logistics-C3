
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.CurrentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.flightcrewmember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class)) {

			// All the flight crew members can access this view.
			if (super.getRequest().getMethod().equals("GET"))
				isAuthorised = true;

			// Only is allowed to create a flight assignment if post method include a valid flight assignment and leg selected.
			if (super.getRequest().getMethod().equals("POST") && super.getRequest().getData("id", Integer.class) != null && super.getRequest().getData("leg", Integer.class) != null) {

				FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

				// Only is allowed to create a flight assignment if the leg selected is between the options shown.
				Collection<Leg> legs = this.repository.findAllLegsByAirlineId(MomentHelper.getCurrentMoment(), flightCrewMember.getAirline().getId());

				Integer legId = super.getRequest().getData("leg", Integer.class);
				Leg legSelected = this.repository.findLegById(legId);

				isAuthorised = (legSelected != null && legs.contains(legSelected) || legId == 0) && super.getRequest().getData("id", Integer.class).equals(0);

			}

		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment = new FlightAssignment();

		flightAssignment.setMoment(MomentHelper.getCurrentMoment());
		flightAssignment.setDraftMode(true);

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		flightAssignment.setFlightCrewMember(flightCrewMember);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		super.bindObject(flightAssignment, "duty", "currentStatus", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "draftMode", "flightCrewMember", "leg");

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		dataset.put("flightCrewMember", flightCrewMember.getIdentity().getFullName());

		// Duty choices
		SelectChoices dutyChoices = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);
		dataset.put("duty", dutyChoices.getSelected().getKey());

		// Status choices
		SelectChoices statusChoices = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());
		dataset.put("statusChoices", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		// Leg choices
		SelectChoices legChoices = new SelectChoices();
		legChoices.add("0", "---", flightAssignment.getLeg() == null);
		Collection<Leg> legs = this.repository.findAllLegsByAirlineId(MomentHelper.getCurrentMoment(), flightCrewMember.getAirline().getId());
		for (Leg legChoice : legs) {
			String key = Integer.toString(legChoice.getId());
			String label = legChoice.getFlightNumber() + " (" + legChoice.getScheduledDeparture() + " - " + legChoice.getScheduledArrival() + ") ";
			boolean isSelected = legChoice.equals(flightAssignment.getLeg());
			legChoices.add(key, label, isSelected);
		}

		dataset.put("legChoices", legChoices);

		super.getResponse().addData(dataset);
	}

}
