
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;

@GuiService
public class AdministratorAirlineUpdateService extends AbstractGuiService<Administrator, Airline> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int whineId;
		Airline whine;

		whineId = super.getRequest().getData("id", int.class);
		whine = this.repository.findAirline(whineId);
		status = whine != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int whineId;
		Airline whine;

		whineId = super.getRequest().getData("id", int.class);
		whine = this.repository.findAirline(whineId);

		super.getBuffer().addData(whine);
	}

	@Override
	public void bind(final Airline whine) {
		super.bindObject(whine, "name", "code", "website", "type", "foundationMoment", "email", "phoneNumber");
	}

	@Override
	public void validate(final Airline whine) {
		{
			boolean confirmation;

			confirmation = super.getRequest().getData("confirmation", boolean.class);
			super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		}

	}

	@Override
	public void perform(final Airline whine) {
		this.repository.save(whine);
	}

	@Override
	public void unbind(final Airline whine) {
		Dataset dataset;

		dataset = super.unbindObject(whine, "name", "code", "website", "type", "foundationMoment", "email", "phoneNumber");
		dataset.put("confirmation", false);
		super.getResponse().addData(dataset);

	}
}
