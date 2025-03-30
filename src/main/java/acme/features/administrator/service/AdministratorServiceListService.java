
package acme.features.administrator.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.service.Service;

@GuiService
public class AdministratorServiceListService extends AbstractGuiService<Administrator, Service> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorServiceRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Service> service;

		service = this.repository.findAllService();

		super.getBuffer().addData(service);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "name", "picture", "averageDwellTime", "promotionCode", "money");

		super.getResponse().addData(dataset);

	}
}
