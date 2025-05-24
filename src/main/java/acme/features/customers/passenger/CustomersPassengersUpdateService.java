
package acme.features.customers.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Passenger;
import acme.realms.Customers;

@GuiService
public class CustomersPassengersUpdateService extends AbstractGuiService<Customers, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersPassengersRepository repository;

	// AbstractService -------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		Passenger passenger;
		Customers customer;
		if (super.getRequest().hasData("id")) {
			Integer passengerId;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				passengerId = Integer.valueOf(isInteger);
			else
				passengerId = Integer.valueOf(-1);
			passenger = passengerId != null ? this.repository.findPassengerById(passengerId) : null;
			customer = passenger == null ? null : passenger.getCustomer();
			status = customer == null ? false : passenger.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {

		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");

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
