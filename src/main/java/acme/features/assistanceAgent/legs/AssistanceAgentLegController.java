
package acme.features.assistanceAgent.legs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;
import acme.realms.AssistanceAgent;

@GuiController
public class AssistanceAgentLegController extends AbstractGuiController<AssistanceAgent, Leg> {

	// Internal state ---------------------------------------------------------
	
	@Autowired
	private AssistanceAgentLegShowService					showService;

	

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("show", this.showService);
		

	}

}
