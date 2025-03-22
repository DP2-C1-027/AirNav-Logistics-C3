
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
	private CustomersBookingPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Passenger> passenger;

		int id;
		Booking booking;

		id = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookinById(id);

		passenger = this.repository.findPassengersByBookingId(booking.getId());

		super.getBuffer().addData(passenger);
	}

	@Override
	public void unbind(final Passenger booking) {
		Dataset dataset;

		dataset = super.unbindObject(booking, "fullName", "email");
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}
}
