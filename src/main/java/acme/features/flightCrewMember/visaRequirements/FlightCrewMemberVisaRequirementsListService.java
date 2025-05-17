
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

		try {
			// Only is allowed to view visa requirements list if the creator is the flight crew member associated to the flight assignment.
			int assignmentId = super.getRequest().getData("assignmentId", int.class);
			FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
			isAuthorised = flightAssignment != null && super.getRequest().getPrincipal().hasRealm(flightAssignment.getFlightCrewMember());
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
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
		assert visaRequirements != null;

		Dataset dataset = super.unbindObject(visaRequirements, "country", "nationality", "visaRequired", "visaType", "additionalInfo");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<VisaRequirements> visaRequirements) {
		assert visaRequirements != null;

		super.getResponse().addGlobal("assignmentId", super.getRequest().getData("assignmentId", int.class));
	}
}
