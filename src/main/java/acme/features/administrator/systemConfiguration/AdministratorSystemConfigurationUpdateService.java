
package acme.features.administrator.systemConfiguration;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.systemConfiguration.SystemConfiguration;

@GuiService
public class AdministratorSystemConfigurationUpdateService extends AbstractGuiService<Administrator, SystemConfiguration> {

	@Autowired
	private SystemConfigurationRepository repository;


	@Override
	public void authorise() {
		boolean isAuthorised = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		SystemConfiguration config = this.repository.findActive();
		super.getBuffer().addData(config);
	}

	@Override
	public void bind(final SystemConfiguration config) {
		super.bindObject(config, "currency"); // Obtenemos la nueva moneda seleccionada

	}

	@Override
	public void validate(final SystemConfiguration config) {

	}

	@Override
	public void perform(final SystemConfiguration config) {
		// Obtener todas las configuraciones y desactivar todas
		SystemConfiguration currentConfig = this.repository.findById(config.getId());
		SystemConfiguration newActiveConfig = this.repository.findById((int) Integer.valueOf(config.getCurrency()));

		newActiveConfig.setSystemCurrency(true);
		currentConfig.setSystemCurrency(false);
		this.repository.saveAll(List.of(newActiveConfig, currentConfig));

	}

	@Override
	public void unbind(final SystemConfiguration config) {
		Dataset dataset;
		SelectChoices choices;

		Collection<SystemConfiguration> allConfigs = this.repository.getSystem();
		choices = SelectChoices.from(allConfigs, "currency", config);

		dataset = super.unbindObject(config, "currency");

		dataset.put("currency", choices.getSelected().getKey());
		dataset.put("systemCurrencies", choices);

		super.getResponse().addData(dataset);
	}
}
