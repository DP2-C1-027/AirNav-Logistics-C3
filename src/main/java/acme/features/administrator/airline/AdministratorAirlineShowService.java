
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;

@GuiService
public class AdministratorAirlineShowService extends AbstractGuiService<Administrator, Airline> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	@Override
	public void load() {
		Airline announcement;
		int id;

		id = super.getRequest().getData("id", int.class);
		announcement = this.repository.findAirline(id);

		super.getBuffer().addData(announcement);
	}
	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;

		dataset = super.unbindObject(airline, "name", "code", "website", "type", "foundationMoment", "email", "phoneNumber");
		dataset.put("confirmation", false);
		dataset.put("readonly", true);
		super.getResponse().addData(dataset);
	}
}
