
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
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class) && super.getRequest().getData("assignmentId", Integer.class) != null) {

			Integer assignmentId = super.getRequest().getData("assignmentId", Integer.class);
			FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
			FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

			if (flightAssignment != null && !flightAssignment.getDraftMode() && flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()) && flightAssignment.getFlightCrewMember().equals(flightCrewMember)) {

				if (super.getRequest().getMethod().equals("GET"))
					isAuthorised = true;

				// Only is allowed to create an activity log if the creator is the flight crew member associated to the flight assignment.
				// An activity log cannot be created if the assignment is planned, only complete are allowed.
				if (super.getRequest().getMethod().equals("POST") && super.getRequest().getData("id", Integer.class) != null)
					isAuthorised = super.getRequest().getData("id", Integer.class).equals(0);
			}
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		ActivityLog activityLog = new ActivityLog();

		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
		activityLog.setFlightAssignment(flightAssignment);

		activityLog.setDraftMode(true);
		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {

	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset = super.unbindObject(activityLog, "typeOfIncident", "typeOfIncident", "description", "severityLevel", "draftMode");

		dataset.put("assignmentId", super.getRequest().getData("assignmentId", int.class));
		super.getResponse().addData(dataset);
	}

}
