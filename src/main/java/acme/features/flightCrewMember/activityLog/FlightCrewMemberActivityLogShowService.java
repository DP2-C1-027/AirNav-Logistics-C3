
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightcrewmember.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogShowService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class))

			// Only is allowed to show an activity log if the creator is the flight crew member associated to the flight assignment.
			if (super.getRequest().getMethod().equals("GET") && super.getRequest().getData("id", Integer.class) != null) {

				Integer activityLogId = super.getRequest().getData("id", Integer.class);
				ActivityLog activityLog = this.repository.findActivityLogById(activityLogId);

				FlightAssignment flightAssignment = activityLog.getFlightAssignment();
				FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

				isAuthorised = activityLog != null && flightAssignment != null && !flightAssignment.getDraftMode() && flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment())
					&& activityLog.getFlightAssignment().getFlightCrewMember().equals(flightCrewMember);

			}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");

		super.getResponse().addData(dataset);
	}
}
