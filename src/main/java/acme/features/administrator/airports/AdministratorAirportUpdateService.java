
package acme.features.administrator.airports;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.airport.OperationalScope;

@GuiService
public class AdministratorAirportUpdateService extends AbstractGuiService<Administrator, Airport> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirportRepository repository;

	// AbstractGuiService interfaced ------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(Administrator.class)) {
			Integer airportId = super.getRequest().getData("id", Integer.class);

			if (airportId != null) {
				Airport airport = this.repository.findAirport(airportId);
				isAuthorised = airport != null;
			}
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Airport airport = this.repository.findAirport(id);

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "codigo", "city", "country", "website", "email", "phoneNumber");

	}

	@Override
	public void validate(final Airport airport) {
		// Check if the code related with an airport is already used by another airport
		Airport existingAirport = this.repository.findAirportCode(airport.getCodigo());
		boolean uniqueAirport = existingAirport == null || existingAirport.equals(airport);
		super.state(uniqueAirport, "codigo", "customers.booking.error.repeat-code");

		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airport airport) {
		this.repository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset = super.unbindObject(airport, "name", "codigo", "city", "country", "website", "email", "phoneNumber");

		SelectChoices choices = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());
		dataset.put("operationalScope", choices);

		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}

}
