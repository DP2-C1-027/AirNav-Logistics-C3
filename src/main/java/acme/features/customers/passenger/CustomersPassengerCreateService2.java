
package acme.features.customers.passenger;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.Passenger;
import acme.features.customers.bookingRecord.CustomersBookingRecordRepository;
import acme.realms.Customers;

@GuiService
public class CustomersPassengerCreateService2 extends AbstractGuiService<Customers, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersPassengersRepository		repository;

	@Autowired
	private CustomersBookingRecordRepository	bookingRepository;

	// AbstractGuiService interface ------------------------------------------

	BookingRecord								bookingRecord	= new BookingRecord();


	@Override
	public void authorise() {
		boolean status = true;
		if (super.getRequest().hasData("bookingId", int.class)) {
			Integer bookingId;
			try {
				bookingId = super.getRequest().getData("bookingId", int.class);
			} catch (Exception e) {
				bookingId = null;

			}

			Booking booking = bookingId != null ? this.bookingRepository.findBookingById(bookingId) : null;
			Customers customer = booking != null ? booking.getCustomer() : null;

			status = customer == null ? false : booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;

		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.bookingRepository.findBookingById(bookingId);

		Date moment;
		moment = MomentHelper.getCurrentMoment();

		passenger = new Passenger();
		passenger.setDateOfBirth(moment);
		passenger.setCustomer(booking.getCustomer());

		super.getBuffer().addData(passenger);

		if (booking != null)
			this.bookingRecord.setBooking(booking);

	}

	@Override
	public void bind(final Passenger passenger) {

		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
		passenger.setDraftMode(true);

	}

	@Override
	public void validate(final Passenger passenger) {

		;
	}

	@Override
	public void perform(final Passenger passenger) {

		this.repository.save(passenger);
		this.bookingRecord.setPassenger(passenger);
		this.bookingRepository.save(this.bookingRecord);

	}

	@Override
	public void unbind(final Passenger passenger) {

		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);

	}
}
