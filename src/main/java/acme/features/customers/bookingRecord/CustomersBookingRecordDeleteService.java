
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
public class CustomersBookingRecordDeleteService extends AbstractGuiService<Customers, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		Customers customer;
		boolean status;
		Booking booking;
		Passenger passenger;
		customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();
		int bookingRecordId = super.getRequest().getData("id", int.class);
		passenger = this.repository.findOnePassengerByBookingRecord(bookingRecordId);
		booking = this.repository.findOneBookingByBookingRecord(bookingRecordId);
		status = booking != null && passenger != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);
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
		super.state(bookingRecord.getBooking().isDraftMode(), "*", "customers.form.error.draft-mode");
		;
	}

	@Override
	public void perform(final BookingRecord bookingRecord) {

		this.repository.delete(bookingRecord);

	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		Customers customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();

		Collection<Passenger> passenger = this.repository.findPassengerByCustomerId(customer.getId());

		Collection<Booking> booking = this.repository.findNotPublishBooking(customer.getId(), true);

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

		super.getResponse().addData(dataset);
	}

}
