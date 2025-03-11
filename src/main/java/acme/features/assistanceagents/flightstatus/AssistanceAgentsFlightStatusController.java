
package acme.features.assistanceagents.flightstatus;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flights.FlightStatus;

@GuiController
public class AssistanceAgentsFlightStatusController extends AbstractGuiController<Any, FlightStatus> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentsFlightStatusService service;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.service);
	}
}
