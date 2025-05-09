
package acme.features.customers.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.Passenger;
import acme.realms.Customers;

@GuiService
public class CustomersBookingPassengerListService extends AbstractGuiService<Customers, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersPassengersRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;

		int id;
		Booking booking;

		id = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookinById(id);
		Customers customer = booking.getCustomer();

		status = super.getRequest().getPrincipal().hasRealm(customer) || booking != null && !booking.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Passenger> passenger;

		int id;
		Booking booking;

		id = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookinById(id);

		passenger = this.repository.findPassengersByBookingId(booking.getId(), booking.getCustomer().getId());

		super.getBuffer().addData(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email");

		super.getResponse().addData(dataset);
	}
}
