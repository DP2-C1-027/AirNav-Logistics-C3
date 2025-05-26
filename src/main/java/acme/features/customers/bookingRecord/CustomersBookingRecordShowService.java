
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

		if (super.getRequest().hasData("id")) {
			Integer id;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				id = Integer.valueOf(isInteger);
			else
				id = Integer.valueOf(-1);
			BookingRecord bRecord = !id.equals(Integer.valueOf(-1)) ? this.repository.findBookingRecord(id) : null;
			Booking booking = bRecord != null ? this.repository.findOneBookingByBookingRecord(bRecord.getId()) : null;
			customer = booking != null ? booking.getCustomer() : null;
			status = customer == null ? false : super.getRequest().getPrincipal().hasRealm(customer);
			//|| bRecord != null && booking != null && !booking.isDraftMode() && passenger != null && !passenger.isDraftMode();
		} else
			status = false;

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
