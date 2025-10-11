
package acme.features.administrator.airline;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineType;

@GuiService
public class AdministratorAirlineCreateService extends AbstractGuiService<Administrator, Airline> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(Administrator.class)) {
			if (super.getRequest().getMethod().equals("GET"))
				isAuthorised = true;

			// Only is allowed to create an airline if post method include a valid airline.
			if (super.getRequest().getMethod().equals("POST") && super.getRequest().getData("id", Integer.class) != null)
				isAuthorised = super.getRequest().getData("id", Integer.class).equals(0);
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		Airline airline;
		Date moment;

		moment = MomentHelper.getCurrentMoment();

		airline = new Airline();

		airline.setFoundationMoment(moment);

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
		dataset.put("types", choices);
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

}
