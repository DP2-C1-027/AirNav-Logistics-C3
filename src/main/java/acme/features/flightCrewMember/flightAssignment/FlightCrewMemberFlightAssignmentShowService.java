
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
public class FlightCrewMemberFlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class))

			// Only is allowed to show a flight assignment if the creator is a flight crew member associated.
			if (super.getRequest().getMethod().equals("GET") && super.getRequest().getData("id", Integer.class) != null) {

				Integer flightAssignmentId = super.getRequest().getData("id", Integer.class);

				FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
				FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

				isAuthorised = flightAssignment != null && flightAssignment.getFlightCrewMember().equals(flightCrewMember);
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
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "flightCrewMember", "leg", "draftMode");

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
		dataset.put("flightNumber", leg.getFlightNumber());
		dataset.put("scheduledDeparture", leg.getScheduledDeparture());
		dataset.put("scheduledArrival", leg.getScheduledArrival());
		dataset.put("status", leg.getStatus());
		dataset.put("duration", leg.getDuration());
		dataset.put("departureAirport", leg.getDepartureAirport().getName());
		dataset.put("arrivalAirport", leg.getArrivalAirport().getName());
		dataset.put("aircraft", leg.getAircraft().getRegistrationNumber());
		dataset.put("flight", leg.getFlight().getTag());
		dataset.put("legAirline", leg.getAircraft().getAirline().getName());

		// Show activity logs if the assignment is related with completed legs
		if (flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()) && !flightAssignment.getDraftMode())
			super.getResponse().addGlobal("showActivityLogs", true);

		super.getResponse().addData(dataset);
	}
}
