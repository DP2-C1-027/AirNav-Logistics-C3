
package acme.features.flightCrewMember.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiController
public class FlightCrewMemberFlightAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentListPublishedService	listPublishedService;

	@Autowired
	private FlightCrewMemberFlightAssignmentListPlannedService		listPlannedService;

	@Autowired
	private FlightCrewMemberFlightAssignmentListCompletedService	listCompletedService;

	@Autowired
	private FlightCrewMemberFlightAssignmentShowService				showService;

	@Autowired
	private FlightCrewMemberFlightAssignmentCreateService			createService;

	@Autowired
	private FlightCrewMemberFlightAssignmentUpdateService			updateService;

	@Autowired
	private FlightCrewMemberFlightAssignmentPublishService			publishService;

	@Autowired
	private FlightCrewMemberFlightAssignmentDeleteService			deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-published", "list", this.listPublishedService);
		super.addCustomCommand("list-planned", "list", this.listPlannedService);
		super.addCustomCommand("list-completed", "list", this.listCompletedService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);
		super.addBasicCommand("delete", this.deleteService);
	}
}
