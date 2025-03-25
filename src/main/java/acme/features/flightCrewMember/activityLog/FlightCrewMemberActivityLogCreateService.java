
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

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
		int flightAssignmentId = super.getRequest().getData("flightAssignmentId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		ActivityLog activityLog = new ActivityLog();
		activityLog.setTypeOfIncident("");
		activityLog.setDescription("");
		activityLog.setSeverityLevel(0);
		activityLog.setFlightAssignment(flightAssignment);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
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
		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");

		dataset.put("flightAssignmentId", super.getRequest().getData("flightAssignmentId", int.class));

		super.getResponse().addData(dataset);
	}

}
