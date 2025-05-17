
package acme.features.customers.passenger;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Passenger;
import acme.realms.Customers;

@GuiService
public class CustomersPassengerCreateService extends AbstractGuiService<Customers, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersPassengersRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Customers customer;

		customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();
		status = super.getRequest().getPrincipal().hasRealm(customer);
		if (super.getRequest().hasData("id", int.class)) {
			Integer id;
			try {
				id = super.getRequest().getData("id", Integer.class);
				if (!id.equals(Integer.valueOf(0)))
					status = false;
			} catch (Exception e) {
				status = false;
			}

		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		Customers customer;

		customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();
		Date moment;
		moment = MomentHelper.getCurrentMoment();

		passenger = new Passenger();
		passenger.setDateOfBirth(moment);
		passenger.setCustomer(customer);
		super.getBuffer().addData(passenger);

	}

	@Override
	public void bind(final Passenger passenger) {

		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
		passenger.setDraftMode(true);

	}

	@Override
	public void validate(final Passenger passenger) {

		;
	}

	@Override
	public void perform(final Passenger passenger) {

		this.repository.save(passenger);

	}

	@Override
	public void unbind(final Passenger passenger) {

		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");

		super.addPayload(dataset, passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);

	}
}
