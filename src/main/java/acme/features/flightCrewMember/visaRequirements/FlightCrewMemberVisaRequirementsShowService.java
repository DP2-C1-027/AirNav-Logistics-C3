
package acme.features.flightCrewMember.visaRequirements;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.visaRequirements.VisaRequirements;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberVisaRequirementsShowService extends AbstractGuiService<FlightCrewMember, VisaRequirements> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberVisaRequirementsRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		try {
			// Only is allowed to show an activity log if the creator is the flight crew member associated to the flight assignment.
			int visaRequirementsId = super.getRequest().getData("id", int.class);
			VisaRequirements visaRequirements = this.repository.findVisaRequirementsById(visaRequirementsId);
			isAuthorised = visaRequirements != null && super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		VisaRequirements visaRequirements = this.repository.findVisaRequirementsById(id);

		super.getBuffer().addData(visaRequirements);
	}

	@Override
	public void unbind(final VisaRequirements visaRequirements) {
		Dataset dataset = super.unbindObject(visaRequirements, "country", "nationality", "visaRequired", "visaType", "additionalInfo");

		super.getResponse().addData(dataset);
	}
}
