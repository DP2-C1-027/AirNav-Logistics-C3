
package acme.features.customers.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.Passenger;
import acme.realms.Customers;

@GuiService
public class CustomersBookingRecordListService extends AbstractGuiService<Customers, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRecordRepository repository;


	//  ---------------------------
	@Override
	public void authorise() {
		Customers customer;
		boolean status;
		customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();

		status = super.getRequest().getPrincipal().hasRealm(customer);
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<BookingRecord> booking;
		int customerId;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		booking = this.repository.findAllBRecordByCustomerId(customerId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		Passenger passenger;
		Booking booking;
		int bookingRecordId = bookingRecord.getId();
		passenger = this.repository.findOnePassengerByBookingRecord(bookingRecordId);
		booking = this.repository.findOneBookingByBookingRecord(bookingRecordId);

		dataset = super.unbindObject(bookingRecord, "booking", "passenger");
		dataset.put("booking", booking.getLocatorCode());
		dataset.put("passenger", passenger.getFullName());
		super.addPayload(dataset, bookingRecord);

		super.getResponse().addData(dataset);

	}

}
