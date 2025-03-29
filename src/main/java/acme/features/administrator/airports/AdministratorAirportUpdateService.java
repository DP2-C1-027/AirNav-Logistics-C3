
package acme.features.administrator.airports;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;

@GuiService
public class AdministratorAirportUpdateService extends AbstractGuiService<Administrator, Airport> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirportRepository repository;

	// AbstractGuiService interfaced ------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airport airport;
		int id;

		id = super.getRequest().getData("id", int.class);
		airport = this.repository.findAirport(id);

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {

		super.bindObject(airport, "name", "codigo", "operationalScope", "city", "country", "website", "email", "address", "phoneNumber");
	}

	@Override
	public void validate(final Airport airport) {
		;
	}

	@Override
	public void perform(final Airport airport) {
		this.repository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;

		dataset = super.unbindObject(airport, "name", "codigo", "operationalScope", "city", "country", "website", "email", "address", "phoneNumber");

		super.getResponse().addData(dataset);
	}

}
