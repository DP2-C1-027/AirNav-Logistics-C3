
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.ActivityLog;
import acme.entities.flightAssignment.CurrentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.flightcrewmember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class))

			// Only is allowed to delete an flight assignment if the creator is associated.
			// A flight assignment cannot be deleted if is published, only in draft mode is allowed.
			if (super.getRequest().getData("id", Integer.class) != null) {

				Integer flightAssignmentId = super.getRequest().getData("id", Integer.class);

				if (flightAssignmentId != null) {

					FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
					FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

					isAuthorised = flightAssignment != null && flightAssignment.getDraftMode() && flightAssignment.getFlightCrewMember().equals(flightCrewMember);
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

	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		Collection<ActivityLog> activityLogs = this.repository.findAllActivityLogs(flightAssignment.getId());
		this.repository.deleteAll(activityLogs);
		this.repository.delete(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "draftMode", "flightCrewMember", "leg");

		FlightCrewMember flightCrewMember = flightAssignment.getFlightCrewMember();
		Leg leg = flightAssignment.getLeg();

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
		legChoices.add("0", "---", leg == null);
		Collection<Leg> legs = this.repository.findAllLegsByAirlineId(MomentHelper.getCurrentMoment(), flightCrewMember.getAirline().getId());
		for (Leg legChoice : legs) {
			String key = Integer.toString(legChoice.getId());
			String label = legChoice.getFlightNumber() + " (" + legChoice.getScheduledDeparture() + " - " + legChoice.getScheduledArrival() + ") ";
			boolean isSelected = legChoice.equals(leg);
			legChoices.add(key, label, isSelected);
		}

		dataset.put("legChoices", legChoices);

		// Flight Crew Member details
		dataset.put("flightCrewMember", flightCrewMember.getIdentity().getFullName());
		dataset.put("codigo", flightCrewMember.getCodigo());
		dataset.put("phoneNumber", flightCrewMember.getPhoneNumber());
		dataset.put("languageSkills", flightCrewMember.getLanguageSkills());
		dataset.put("availabilityStatus", flightCrewMember.getAvailabilityStatus());
		dataset.put("salary", flightCrewMember.getSalary());
		dataset.put("yearsOfExperience", flightCrewMember.getYearsOfExperience());
		dataset.put("airline", flightCrewMember.getAirline().getName());

		// Leg details
		Leg originalLeg = this.repository.findFlightAssignmentById(flightAssignment.getId()).getLeg();
		dataset.put("flightNumber", originalLeg.getFlightNumber());
		dataset.put("scheduledDeparture", originalLeg.getScheduledDeparture());
		dataset.put("scheduledArrival", originalLeg.getScheduledArrival());
		dataset.put("status", originalLeg.getStatus());
		dataset.put("duration", originalLeg.getDuration());
		dataset.put("departureAirport", originalLeg.getDepartureAirport().getName());
		dataset.put("arrivalAirport", originalLeg.getArrivalAirport().getName());
		dataset.put("aircraft", originalLeg.getAircraft().getRegistrationNumber());
		dataset.put("flight", originalLeg.getFlight().getTag());
		dataset.put("legAirline", originalLeg.getAircraft().getAirline().getName());

		super.getResponse().addData(dataset);
	}

}
