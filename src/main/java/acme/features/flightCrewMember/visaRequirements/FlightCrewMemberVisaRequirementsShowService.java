
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

		if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class))

			// Only is allowed to show a visa requirement if the creator is a flight crew member.
			if (super.getRequest().getMethod().equals("GET") && super.getRequest().getData("id", Integer.class) != null) {

				Integer visaRequirementId = super.getRequest().getData("id", Integer.class);

				if (visaRequirementId != null) {
					VisaRequirements visaRequirements = this.repository.findVisaRequirementsById(visaRequirementId);

					isAuthorised = visaRequirements != null;
				}
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
