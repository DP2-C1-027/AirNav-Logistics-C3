
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

		if (super.getRequest().hasData("bookingId", int.class)) {
			Integer bookingId;
			try {
				bookingId = super.getRequest().getData("bookingId", Integer.class);
			} catch (Exception e) {
				bookingId = null;

			}

			Booking booking = bookingId != null ? this.repository.findBookingById(bookingId) : null;
			Customers customer = booking != null ? booking.getCustomer() : null;

			status = customer == null ? false : booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

			if (super.getRequest().hasData("passenger")) {
				Integer id;
				try {
					id = super.getRequest().getData("passenger", Integer.class);
					passenger = this.repository.findPassengerById(id);

					if (!id.equals(Integer.valueOf(0)) && !passenger.getCustomer().equals(customer))
						status = false;

				} catch (Exception e) {
					status = false;
				}
			} else if (super.getRequest().getMethod().equals("POST"))
				status = false;

		}

		if (super.getRequest().hasData("id")) {
			Integer id;
			try {
				id = super.getRequest().getData("id", Integer.class);
				if (!id.equals(Integer.valueOf(0)))
					status = false;

			} catch (Exception e) {
				status = false;

			}
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
