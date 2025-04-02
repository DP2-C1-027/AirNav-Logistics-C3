
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.ActivityLog;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		// Only is allowed to publish an activity log if the creator is the flight crew member associated to the flight assignment.
		// An activity log cannot be published if:
		// - Activity log should be in draft mode and not published.
		// - Assignment should be in published mode and not in draft mode.
		int activityLogId = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findActivityLogById(activityLogId);
		boolean status = activityLog != null && activityLog.getDraftMode() && !activityLog.getFlightAssignment().getDraftMode() && super.getRequest().getPrincipal().hasRealm(activityLog.getFlightAssignment().getFlightCrewMember());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {

	}

	@Override
	public void validate(final ActivityLog activityLog) {

		if (activityLog.getFlightAssignment() != null && activityLog.getFlightAssignment().getDraftMode() == true)
			// TODO: Añadir código de estado
			super.state(false, "flightAssignment", "acme.validation.activityLog.flightAssignment");
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		activityLog.setDraftMode(false);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment", "draftMode");

		// Show create if the assignment is completed
		if (activityLog.getFlightAssignment().getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()))
			super.getResponse().addGlobal("showAction", true);

		super.getResponse().addData(dataset);
	}

}
