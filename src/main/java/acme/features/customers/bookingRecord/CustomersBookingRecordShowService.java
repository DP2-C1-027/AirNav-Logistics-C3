
package acme.features.customers.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
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
				id = super.getRequest().getData("id", Integer.class);
			} catch (Exception e) {
				id = null;

			}
			BookingRecord bRecord = id != null ? this.repository.findBookingRecord(id) : null;
			Booking booking = bRecord != null ? this.repository.findOneBookingByBookingRecord(bRecord.getId()) : null;
			customer = booking != null ? booking.getCustomer() : null;
			status = customer == null ? false : super.getRequest().getPrincipal().hasRealm(customer);
			//|| bRecord != null && booking != null && !booking.isDraftMode() && passenger != null && !passenger.isDraftMode();
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

		Booking boog = this.repository.findOneBookingByBookingRecord(bookingRecord.getId());

		Passenger passemger = bookingRecord.getPassenger();

		dataset = super.unbindObject(bookingRecord, "booking", "passenger");

		dataset.put("booking", boog.getLocatorCode());
		dataset.put("passenger", passemger.getFullName());

		dataset.put("draftMode", bookingRecord.getBooking().isDraftMode());
		super.addPayload(dataset, bookingRecord, "booking", "passenger");

		super.getResponse().addData(dataset);

	}

}
