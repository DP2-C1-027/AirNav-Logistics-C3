
package acme.features.assistanceAgent.claims;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claims.Claim;
import acme.realms.AssistanceAgent;

@GuiController
public class AssistanceAgentClaimController extends AbstractGuiController<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimListServiceCompleted		listService;
	@Autowired
	private AssistanceAgentClaimShowService					showService;

	@Autowired
	private AssistanceAgentClaimShowByTrackingLogService	showByTrackingLog;

	@Autowired
	private AssistanceAgentClaimListUndergoingService		listUndergoingService;

	@Autowired
	private AssistanceAgentClaimCreateService				createService;

	@Autowired
	private AssistanceAgentClaimUpdateService				updateService;

	@Autowired
	private AssistanceAgentClaimDeleteService				deleteService;

	@Autowired
	private AssistanceAgentClaimPublishService				publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {

		super.addCustomCommand("listCompleted", "list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addCustomCommand("showByTrackingLog", "show", this.showByTrackingLog);

		super.addCustomCommand("listUndergoing", "list", this.listUndergoingService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);

	}

}
