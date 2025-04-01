
package acme.features.administrator.trackingLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claims.TrackingLog;

@GuiController
public class AdministratorTrackingLogController extends AbstractGuiController<Administrator, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorTrackingLogListByClaimService listByClaimService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {

		super.addCustomCommand("listByClaim", "list", this.listByClaimService);

	}

}
