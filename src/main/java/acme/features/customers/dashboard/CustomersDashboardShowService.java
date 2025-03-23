
package acme.features.customers.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.CustomersDashboards;
import acme.realms.Customers;

@GuiService
public class CustomersDashboardShowService extends AbstractGuiService<Customers, CustomersDashboards> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

}
