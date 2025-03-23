
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

		super.getResponse().setAuthorised(true);
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
		//	super.bindObject(whine, "name", "code", "website", "type", "foundationMoment", "email", "phoneNumber");
		super.bindObject(airline, "name", "codigo", "website", "type", "foundationMoment", "email", "phoneNumber");

	}

	@Override
	public void validate(final Airline airline) {
		{
			boolean confirmation;

			confirmation = super.getRequest().getData("confirmation", boolean.class);
			super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		}

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
