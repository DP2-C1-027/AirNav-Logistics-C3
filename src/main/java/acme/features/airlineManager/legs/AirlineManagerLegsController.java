
package acme.features.airlineManager.legs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@GuiController
public class AirlineManagerLegsController extends AbstractGuiController<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegsShowService	showService;

	@Autowired
	private AirlineManagerLegsListService	listService;

	@Autowired
	private AirlineManagerLegsCreateService	createService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
	}
}
