
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineType;

@GuiService
public class AdministratorAirlineUpdateService extends AbstractGuiService<Administrator, Airline> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineRepository repository;

	// AbstractService -------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(Administrator.class)) {
			Integer airlineId = super.getRequest().getData("id", Integer.class);

			if (airlineId != null) {
				Airline airline = this.repository.findAirlineById(airlineId);
				isAuthorised = airline != null && super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
			}
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("id", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {

		super.bindObject(airline, "name", "codigo", "website", "type", "foundationMoment", "email", "phoneNumber");

	}

	@Override
	public void validate(final Airline airline) {
		// Check if the code related with an airline is already used by another airline
		Airline existingAirline = this.repository.findAirlineCode(airline.getCodigo());
		boolean uniqueAirline = existingAirline == null || existingAirline.equals(airline);
		super.state(uniqueAirline, "codigo", "customers.booking.error.repeat-code");

		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");

	}

	@Override
	public void perform(final Airline airline) {

		this.repository.save(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(AirlineType.class, airline.getType());
		dataset = super.unbindObject(airline, "name", "codigo", "website", "type", "foundationMoment", "email", "phoneNumber");
		dataset.put("confirmation", false);
		dataset.put("types", choices);
		super.getResponse().addData(dataset);

	}
}
