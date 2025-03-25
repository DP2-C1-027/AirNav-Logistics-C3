
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.ActivityLog;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		Collection<ActivityLog> activityLogs = this.repository.findActivityLogsByFlightAssignmentId(assignmentId);
		super.getBuffer().addData(activityLogs);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		super.getResponse().addGlobal("assignmentId", assignmentId);
		super.getResponse().addData(dataset);
	}

}
