
package acme.features.administrator.service;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.service.Service;

@GuiService
public class AdministratorServiceUpdateService extends AbstractGuiService<Administrator, Service> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorServiceRepository repository;

	// AbstractGuiService interfaced ------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(Administrator.class)) {
			Integer serviceId = super.getRequest().getData("id", Integer.class);

			if (serviceId != null) {
				Service service = this.repository.findService(serviceId);
				isAuthorised = service != null;
			}
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		Service service;
		int id;

		id = super.getRequest().getData("id", int.class);
		service = this.repository.findService(id);

		super.getBuffer().addData(service);
	}

	@Override
	public void bind(final Service service) {

		super.bindObject(service, "name", "picture", "averageDwellTime", "promotionCode", "money");

	}

	@Override
	public void validate(final Service service) {

	}

	@Override
	public void perform(final Service service) {
		this.repository.save(service);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "name", "picture", "averageDwellTime", "promotionCode", "money");
		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}

}
