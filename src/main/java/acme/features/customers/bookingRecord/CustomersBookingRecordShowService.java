
package acme.features.customers.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.Passenger;
import acme.realms.Customers;

@GuiService
public class CustomersBookingRecordShowService extends AbstractGuiService<Customers, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRecordRepository repository;


	// ---------------------------
	@Override
	public void authorise() {
		Customers customer;
		boolean status = true;

		if (super.getRequest().hasData("id", int.class)) {
			Integer id;
			try {
				id = super.getRequest().getData("id", int.class);
			} catch (Exception e) {
				id = null;
			}
			BookingRecord bRecord = id != null ? this.repository.findBookingRecord(id) : null;
			Booking booking = id != null ? this.repository.findOneBookingByBookingRecord(id) : null;
			Passenger passenger = id != null ? this.repository.findOnePassengerByBookingRecord(id) : null;
			customer = booking != null ? booking.getCustomer() : null;
			status = customer == null ? false : super.getRequest().getPrincipal().hasRealm(customer) || bRecord != null && booking != null && !booking.isDraftMode() && passenger != null && !passenger.isDraftMode();
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		BookingRecord bRecord = this.repository.findBookingRecord(id);

		super.getBuffer().addData(bRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		SelectChoices passengerChoices;
		SelectChoices bookingChoices;

		Booking boog = this.repository.findOneBookingByBookingRecord(bookingRecord.getId());

		Customers customer = boog.getCustomer();
		Collection<Passenger> passengers = this.repository.findPassengerByCustomerId(customer.getId());
		Collection<Booking> bookings = this.repository.findBookingByCustomerId(customer.getId());
		Collection<Booking> booking = this.repository.findNotPublishBooking(customer.getId());

		passengerChoices = SelectChoices.from(passengers, "fullName", bookingRecord.getPassenger());

		dataset = super.unbindObject(bookingRecord, "booking", "passenger");

		if (!bookingRecord.getBooking().isDraftMode()) {
			bookingChoices = SelectChoices.from(bookings, "locatorCode", bookingRecord.getBooking());
			dataset.put("bookings", bookingChoices);

		} else {
			bookingChoices = SelectChoices.from(booking, "locatorCode", bookingRecord.getBooking());
			dataset.put("booking", bookingChoices.getSelected().getKey());
			dataset.put("bookings", bookingChoices);

		}

		dataset.put("passenger", passengerChoices.getSelected().getKey());
		dataset.put("passengers", passengerChoices);
		dataset.put("draftMode", bookingRecord.getBooking().isDraftMode());
		super.addPayload(dataset, bookingRecord, "booking", "passenger");

		super.getResponse().addData(dataset);

	}

}
