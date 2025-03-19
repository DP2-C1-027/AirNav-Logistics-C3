
package acme.features.customers.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Passenger;
import acme.realms.Customers;

@GuiService
public class CustomersPassengersShowService extends AbstractGuiService<Customers, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersPassengersRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		boolean status;
		int customerId;
		int passengerId;
		//como un passenger puede estar asociado a mas de un customer simplemente me quedo con uno?¿¿¿

		//como hago para pillar el customer
		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		passengerId = super.getRequest().getData("id", int.class);

		status = super.getRequest().getPrincipal().hasRealm(this.repository.findCustomerById(customerId));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int passengerId;
		//Booking booking;
		Passenger passenger;

		passengerId = super.getRequest().getData("id", int.class);

		passenger = this.repository.findPassengerById(passengerId);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");

		super.getResponse().addData(dataset);
	}
}
