
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
public class CustomersBookingRecordDeleteService extends AbstractGuiService<Customers, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		Customers customer;
		boolean status = true;
		Booking booking;
		Passenger passenger;
		if (super.getRequest().hasData("id")) {
			Integer bookingRecordId;

			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				bookingRecordId = Integer.valueOf(isInteger);
			else
				bookingRecordId = Integer.valueOf(-1);

			passenger = bookingRecordId != null ? this.repository.findOnePassengerByBookingRecord(bookingRecordId) : null;
			booking = bookingRecordId != null ? this.repository.findOneBookingByBookingRecord(bookingRecordId) : null;
			customer = booking != null ? booking.getCustomer() : null;
			status = customer == null ? false : booking != null && passenger != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		BookingRecord bookingRecord;
		int id;
		id = super.getRequest().getData("id", int.class);
		bookingRecord = this.repository.findBookingRecord(id);

		super.getBuffer().addData(bookingRecord);

	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		super.bindObject(bookingRecord);
	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		;
	}

	@Override
	public void perform(final BookingRecord bookingRecord) {

		this.repository.delete(bookingRecord);

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
