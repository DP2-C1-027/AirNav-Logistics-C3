
package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;

@GuiService
public class AdministratorBookingListService extends AbstractGuiService<Administrator, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Booking> bookings;

		bookings = this.repository.findAllPublishedBooking();

		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking bookings) {
		Dataset dataset;

		dataset = super.unbindObject(bookings, "locatorCode", "purchaseMoment", "travelClass", "lastNibble");
		//super.addPayload(dataset, bookings, "text");
		dataset.put("price", bookings.getPrice());
		super.getResponse().addData(dataset);
	}
}
