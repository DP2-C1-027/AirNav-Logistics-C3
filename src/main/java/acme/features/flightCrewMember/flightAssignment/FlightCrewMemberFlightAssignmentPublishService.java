
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
import acme.realms.AvailabilityStatus;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class))

			// Only is allowed to publish a flight assignment if the creator is associated.
			// A flight assignment cannot be published if the assignment is in published mode and not in draft mode.
			if (super.getRequest().getMethod().equals("POST") && super.getRequest().hasData("id", Integer.class)) {

				Integer flightAssignmentId = super.getRequest().getData("id", Integer.class);

				if (flightAssignmentId != null) {

					FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
					FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

					// Only is allowed to publish a flight assignment if the leg selected is between the options shown.
					Collection<Leg> legs = this.repository.findAllLegsByAirlineId(flightCrewMember.getAirline().getId());
					Leg legSelected = super.getRequest().getData("leg", Leg.class);

					isAuthorised = flightAssignment != null && flightAssignment.getDraftMode() && flightAssignment.getFlightCrewMember().equals(flightCrewMember) && (legSelected == null || legs.contains(legSelected));

				}

			}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int flightAssignmentId = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		super.bindObject(flightAssignment, "duty", "currentStatus", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		if (flightAssignment.getFlightCrewMember() != null) {
			// Only flight crew members with an "AVAILABLE" status can be assigned 
			boolean isAvailable = flightAssignment.getFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);
			super.state(isAvailable, "flightCrewMember", "acme.validation.flightAssignment.flightCrewMember.available");

			// Cannot be assigned to multiple legs simultaneously
			boolean isAlreadyAssigned = this.repository.hasFlightCrewMemberLegAssociated(flightAssignment.getFlightCrewMember().getId(), MomentHelper.getCurrentMoment());
			super.state(!isAlreadyAssigned, "flightCrewMember", "acme.validation.flightAssignment.flightCrewMember.multipleLegs");
		}

		// To publish a flight assignment these cannot be linked to legs that are not published, do not belongs to the flight crew member airline or have already occurred
		if (flightAssignment.getLeg() != null) {
			boolean isDraftModeLeg = flightAssignment.getLeg().isDraftMode();
			super.state(!isDraftModeLeg, "leg", "acme.validation.flightAssignment.leg.draftmode");

			boolean isLinkedToPastLeg = flightAssignment.getLeg().getScheduledDeparture().before(MomentHelper.getCurrentMoment());
			super.state(!isLinkedToPastLeg, "leg", "acme.validation.flightAssignment.leg.moment");
		}

		// Each leg can only have one pilot and one co-pilot
		if (flightAssignment.getDuty() != null && flightAssignment.getLeg() != null) {
			boolean isDutyAlreadyAssigned = this.repository.hasDutyAssigned(flightAssignment.getLeg().getId(), flightAssignment.getDuty(), flightAssignment.getId());
			super.state(!isDutyAlreadyAssigned, "duty", "acme.validation.flightAssignment.duty");
		}

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		flightAssignment.setDraftMode(false);
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "draftMode", "flightCrewMember", "leg");

		dataset.put("flightCrewMember", flightAssignment.getFlightCrewMember().getIdentity().getFullName());

		// Duty choices
		SelectChoices dutyChoices = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);
		dataset.put("duty", dutyChoices.getSelected().getKey());

		// Status choices
		SelectChoices statusChoices = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());
		dataset.put("statusChoices", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		// Leg choices
		SelectChoices legChoices = SelectChoices.from(this.repository.findAllLegsByAirlineId(flightAssignment.getFlightCrewMember().getAirline().getId()), "flightNumber", flightAssignment.getLeg());
		dataset.put("legChoices", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
