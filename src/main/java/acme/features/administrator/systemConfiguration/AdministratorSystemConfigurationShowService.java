
package acme.features.administrator.systemConfiguration;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.systemConfiguration.SystemConfiguration;

@GuiService
public class AdministratorSystemConfigurationShowService extends AbstractGuiService<Administrator, SystemConfiguration> {

	@Autowired
	private SystemConfigurationRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		SystemConfiguration config = this.repository.findActive();
		super.getBuffer().addData(config);
	}

	@Override
	public void unbind(final SystemConfiguration config) {
		Dataset dataset;
		SelectChoices choices;

		Collection<SystemConfiguration> allConfigs = this.repository.getSystem();
		choices = SelectChoices.from(allConfigs, "currency", config);

		dataset = super.unbindObject(config);

		dataset.put("currency", choices.getSelected().getKey());
		dataset.put("systemCurrencies", choices);

		super.getResponse().addData(dataset);
	}
}
