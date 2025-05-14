
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
public class CustomersBookingRecordUpdateService extends AbstractGuiService<Customers, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRecordRepository repository;


	// AbstractGuiService<Customers, BookingRecord> ---------------------------
	@Override
	public void authorise() {
		Customers customer;
		boolean status = true;
		Booking booking;
		Passenger passenger;
		if (super.getRequest().hasData("id", int.class)) {
			Integer bookingRecordId;
			try {
				bookingRecordId = super.getRequest().getData("id", int.class);
			} catch (Exception e) {
				bookingRecordId = null;
			}
			passenger = bookingRecordId != null ? this.repository.findOnePassengerByBookingRecord(bookingRecordId) : null;
			booking = bookingRecordId != null ? this.repository.findOneBookingByBookingRecord(bookingRecordId) : null;
			customer = booking != null ? booking.getCustomer() : null;
			status = customer == null ? false : booking != null && passenger != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);
			if (super.getRequest().hasData("passenger")) {
				Integer id;
				try {
					id = super.getRequest().getData("passenger", Integer.class);
					passenger = this.repository.findPassengerById(id);

					if (!passenger.getCustomer().equals(customer))
						status = false;

				} catch (Exception e) {
					status = false;
				}
			}

			if (super.getRequest().hasData("booking")) {
				Integer id;
				try {
					id = super.getRequest().getData("booking", Integer.class);
					booking = this.repository.findBookingById(id);

					if (!booking.getCustomer().equals(customer))
						status = false;

				} catch (Exception e) {
					status = false;
					booking = null;
				}
				status = booking != null ? status && booking.isDraftMode() : status;
			}

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

		super.bindObject(bookingRecord, "booking", "passenger");
	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		Booking booking;
		Passenger passenger;

		booking = bookingRecord.getBooking();
		passenger = bookingRecord.getPassenger();

		super.state(booking != null, "booking", "customer.booking-record.create.error.null-booking");
		super.state(passenger != null, "passenger", "customer.booking-record.create.error.null-passenger");
		if (booking != null)
			super.state(booking.isDraftMode(), "booking", "customer.booking-record.create.publish.booking");
		boolean exists = this.repository.existsByBookingAndPassengerExcludingId(booking, passenger, bookingRecord.getId());

		super.state(!exists, "*", "customer.booking-record.create.error.duplicate-booking-passenger");

	}

	@Override
	public void perform(final BookingRecord bookingRecord) {

		this.repository.save(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		Customers customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();

		Collection<Passenger> passenger = this.repository.findPassengerByCustomerId(customer.getId());

		Collection<Booking> booking = this.repository.findNotPublishBooking(customer.getId());

		SelectChoices passengerChoices;
		SelectChoices bookingChoices;

		passengerChoices = SelectChoices.from(passenger, "fullName", bookingRecord.getPassenger());
		bookingChoices = SelectChoices.from(booking, "locatorCode", bookingRecord.getBooking());

		dataset = super.unbindObject(bookingRecord, "booking", "passenger");
		dataset.put("booking", bookingChoices.getSelected().getKey());
		dataset.put("bookings", bookingChoices);
		dataset.put("passenger", passengerChoices.getSelected().getKey());
		dataset.put("passengers", passengerChoices);
		dataset.put("draftMode", bookingRecord.getBooking().isDraftMode());
		super.addPayload(dataset, bookingRecord, "booking", "passenger");

		super.getResponse().addData(dataset);

	}
}
