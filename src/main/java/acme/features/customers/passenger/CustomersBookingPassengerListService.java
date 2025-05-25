
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

		boolean status = true;
		Integer id;
		Booking booking;

		if (super.getRequest().hasData("bookingId")) {

			String isInteger;
			isInteger = super.getRequest().getData("bookingId", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				id = Integer.valueOf(isInteger);
			else
				id = Integer.valueOf(-1);
			booking = !id.equals(Integer.valueOf(-1)) ? this.repository.findBookinById(id) : null;
			Customers customer = booking != null ? booking.getCustomer() : null;

			status = customer == null ? false : super.getRequest().getPrincipal().hasRealm(customer); //|| booking != null && !booking.isDraftMode();
		} else
			status = false;

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

		dataset = super.unbindObject(passenger, "fullName");
		super.addPayload(dataset, passenger);

		super.getResponse().addData(dataset);
	}
}
