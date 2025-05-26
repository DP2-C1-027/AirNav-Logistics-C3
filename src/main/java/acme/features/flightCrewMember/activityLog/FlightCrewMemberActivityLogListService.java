
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class))

			// Only is allowed to view an activity log list if the creator is the flight crew member associated to the flight assignment.
			if (super.getRequest().getMethod().equals("GET") && super.getRequest().getData("assignmentId", Integer.class) != null) {

				Integer assignmentId = super.getRequest().getData("assignmentId", Integer.class);
				FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);

				if (flightAssignment != null) {
					FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

					isAuthorised = flightAssignment.getFlightCrewMember().equals(flightCrewMember);
				}

			}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		Collection<ActivityLog> activityLogs = this.repository.findActivityLogsByFlightAssignmentId(assignmentId);
		super.getBuffer().addData(activityLogs);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");

		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);

		// Show create if the assignment is completed
		if (flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()))
			super.getResponse().addGlobal("showAction", true);

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<ActivityLog> activityLogs) {
		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);

		// Show create if the assignment is completed
		if (flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()))
			super.getResponse().addGlobal("showAction", true);

		super.getResponse().addGlobal("assignmentId", super.getRequest().getData("assignmentId", int.class));
	}

}
