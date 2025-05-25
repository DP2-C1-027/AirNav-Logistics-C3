
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
public class CustomersBookingRecordCreateService extends AbstractGuiService<Customers, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRecordRepository repository;


	@Override
	public void authorise() {

		boolean status = true;
		Passenger passenger;
		Customers customer;

		if (super.getRequest().hasData("bookingId")) {
			Integer bookingId;
			String isInteger;
			isInteger = super.getRequest().getData("bookingId", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				bookingId = Integer.valueOf(isInteger);
			else
				bookingId = Integer.valueOf(-1);

			Booking booking = !bookingId.equals(Integer.valueOf(-1)) ? this.repository.findBookingById(bookingId) : null;
			customer = booking != null ? booking.getCustomer() : null;

			status = customer == null ? false : booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

		} else
			status = false;

		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}

		if (super.getRequest().hasData("passenger")) {
			Integer id;

			String isInteger2;
			isInteger2 = super.getRequest().getData("passenger", String.class);
			if (!isInteger2.isBlank() && isInteger2.chars().allMatch((e) -> e > 47 && e < 58))
				id = Integer.valueOf(isInteger2);
			else
				id = null;

			passenger = id == null ? null : this.repository.findPassengerById(id);
			customer = passenger == null ? null : passenger.getCustomer();

			status = customer == null ? id != null && id.equals(Integer.valueOf(0)) : super.getRequest().getPrincipal().hasRealm(customer);

		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}

		if (super.getRequest().hasData("id")) {
			Integer id;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				id = Integer.valueOf(isInteger);
			else
				id = Integer.valueOf(-1);

			if (!id.equals(Integer.valueOf(0)))
				status = false;

		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		BookingRecord bookingRecord;

		bookingRecord = new BookingRecord();
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.findBookingById(bookingId);
		bookingRecord.setBooking(booking);

		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {

		super.bindObject(bookingRecord, "passenger");

	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		Booking booking;
		Passenger passenger;

		booking = bookingRecord.getBooking();
		passenger = bookingRecord.getPassenger();
		super.state(passenger != null, "passenger", "customer.booking-record.create.error.null-passenger");
		boolean exists = this.repository.existsByBookingAndPassenger(booking, passenger);
		super.state(!exists, "*", "customer.booking-record.create.error.duplicate-booking-passenger");

	}

	@Override
	public void perform(final BookingRecord bookingRecord) {

		this.repository.save(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;

		Booking bookings = bookingRecord.getBooking();

		Customers customer = bookings.getCustomer();
		Collection<Passenger> passenger = this.repository.findPassengerByCustomerId(customer.getId());

		SelectChoices passengerChoices;

		passengerChoices = SelectChoices.from(passenger, "fullName", bookingRecord.getPassenger());

		dataset = super.unbindObject(bookingRecord, "booking", "passenger");

		dataset.put("booking", bookings.getLocatorCode());

		dataset.put("passenger", passengerChoices.getSelected().getKey());
		dataset.put("passengers", passengerChoices);
		dataset.put("draftMode", true);

		super.addPayload(dataset, bookingRecord, "booking", "passenger");

		super.getResponse().addData(dataset);

	}
}
