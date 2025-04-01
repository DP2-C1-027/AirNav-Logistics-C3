
package acme.features.administrator.leg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.legs.Leg;

@GuiController
public class AdministratorLegController extends AbstractGuiController<Administrator, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorLegShowService showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("show", this.showService);

	}

}
