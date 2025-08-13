
package acme.features.administrator.airline;

import java.util.Collection;

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

		try {
			Integer airlineId = super.getRequest().getData("id", Integer.class);
			if (airlineId != null) {
				Airline airline = this.repository.findAirlineById(airlineId);
				isAuthorised = airline != null && super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
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
		String cod = airline.getCodigo();
		Collection<Airline> codigos = this.repository.findAllAirlineCode(cod).stream().filter(x -> x.getId() != airline.getId()).toList();

		if (!codigos.isEmpty())
			super.state(false, "codigo", "customers.booking.error.repeat-code");
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
