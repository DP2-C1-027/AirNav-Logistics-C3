
package acme.features.customers.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.CustomersDashboards;
import acme.realms.Customers;

@GuiController
public class CustomersDashboardController extends AbstractGuiController<Customers, CustomersDashboards> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersDashboardShowService showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}
}
