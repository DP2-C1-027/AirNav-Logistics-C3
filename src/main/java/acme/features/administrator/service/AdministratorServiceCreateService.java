
package acme.features.administrator.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.service.Service;

@GuiService
public class AdministratorServiceCreateService extends AbstractGuiService<Administrator, Service> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorServiceRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(Administrator.class)) {
			if (super.getRequest().getMethod().equals("GET"))
				isAuthorised = true;

			// Only is allowed to create a service if post method include a valid service.
			if (super.getRequest().getMethod().equals("POST") && super.getRequest().getData("id", Integer.class) != null)
				isAuthorised = super.getRequest().getData("id", Integer.class).equals(0);
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		Service service;

		service = new Service();

		super.getBuffer().addData(service);
	}

	@Override
	public void bind(final Service service) {

		super.bindObject(service, "name", "picture", "averageDwellTime", "promotionCode", "money");

	}

	@Override
	public void validate(final Service service) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");

		Collection<Service> codigo;
		boolean confirmation2;
		codigo = this.repository.findByPromotionCode(service.getPromotionCode());
		if (codigo.size() > 0)
			confirmation2 = false;
		else
			confirmation2 = true;
		super.state(confirmation2, "promotionCode", "acme.validation.promotion-code.duplicate-message");

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
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

}
