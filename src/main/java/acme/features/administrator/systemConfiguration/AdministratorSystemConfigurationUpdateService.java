
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
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		SystemConfiguration config = this.repository.findActive();
		super.getBuffer().addData(config);
	}

	@Override
	public void bind(final SystemConfiguration config) {
		super.bindObject(config, "currency"); // Obtenemos la nueva moneda seleccionada
		System.out.print(config.getCurrency());
		// Buscamos la configuración correspondiente a la moneda seleccionada

		System.out.println("Nueva moneda seleccionada: " + config.getCurrency());
	}

	@Override
	public void validate(final SystemConfiguration config) {

	}

	@Override
	public void perform(final SystemConfiguration config) {
		// Obtener todas las configuraciones y desactivar todas
		SystemConfiguration currentConfig = this.repository.findById(config.getId());
		SystemConfiguration newActiveConfig = this.repository.findById((int) Integer.valueOf(config.getCurrency()));

		System.out.print("holi");

		newActiveConfig.setSystemCurrency(true);
		currentConfig.setSystemCurrency(false);
		this.repository.saveAll(List.of(newActiveConfig, currentConfig));
		System.out.println(config);
		//this.repository.saveAll(allConfigs); // Guardamos todas las configuraciones desactivadas
	}

	@Override
	public void unbind(final SystemConfiguration config) {
		Dataset dataset;
		SelectChoices choices;

		Collection<SystemConfiguration> allConfigs = this.repository.getSystem();
		choices = SelectChoices.from(allConfigs, "currency", config);
		//choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		System.out.print("moneda acatual i guess" + config.getCurrency());
		dataset = super.unbindObject(config, "currency");
		System.out.println("estoy en el unbind" + choices.getSelected().getKey());
		dataset.put("currency", choices.getSelected().getKey());
		dataset.put("systemCurrencies", choices); // Aquí va la lista de opciones

		super.getResponse().addData(dataset);
	}
}
