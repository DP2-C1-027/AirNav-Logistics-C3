
package acme.features.customers.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.Passenger;
import acme.realms.Customers;

@GuiService
public class CustomersBookingShowService extends AbstractGuiService<Customers, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookinById(bookingId);
		status = super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookinById(bookingId);

		super.getBuffer().addData(booking);

	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		Collection<Passenger> passenger;

		Money price = booking.getPrice();
		passenger = this.repository.findPassengersByBookingId(booking.getId());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble");
		dataset.put("price", price);

		super.getResponse().addData(dataset);
	}
}
