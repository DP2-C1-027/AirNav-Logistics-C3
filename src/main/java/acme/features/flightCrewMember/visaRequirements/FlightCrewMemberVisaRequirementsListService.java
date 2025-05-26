
package acme.features.flightCrewMember.visaRequirements;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.visaRequirements.VisaRequirements;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberVisaRequirementsListService extends AbstractGuiService<FlightCrewMember, VisaRequirements> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberVisaRequirementsRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class))

			// Only is allowed to show visa requirements if the creator is a flight crew member associated to the flight assignment.
			if (super.getRequest().getMethod().equals("GET") && super.getRequest().getData("assignmentId", Integer.class) != null) {

				Integer assignmentId = super.getRequest().getData("assignmentId", Integer.class);

				if (assignmentId != null) {

					FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
					FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

					isAuthorised = flightAssignment != null && flightAssignment.getFlightCrewMember().equals(flightCrewMember);
				}

			}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
		Collection<VisaRequirements> visaRequirements = this.repository.findAllVisaRequirementsByCountry(flightAssignment.getLeg().getArrivalAirport().getCountry());

		super.getBuffer().addData(visaRequirements);
	}

	@Override
	public void unbind(final VisaRequirements visaRequirements) {
		Dataset dataset = super.unbindObject(visaRequirements, "country", "nationality", "visaRequired", "visaType", "additionalInfo");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<VisaRequirements> visaRequirements) {
		super.getResponse().addGlobal("assignmentId", super.getRequest().getData("assignmentId", int.class));
	}
}
