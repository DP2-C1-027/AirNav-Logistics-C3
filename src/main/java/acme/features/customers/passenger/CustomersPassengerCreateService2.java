
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
				bookingId = super.getRequest().getData("bookingId", Integer.class);
			} catch (Exception e) {
				bookingId = null;

			}

			Booking booking = bookingId != null ? this.bookingRepository.findBookingById(bookingId) : null;
			Customers customer = booking != null ? booking.getCustomer() : null;

			status = customer == null ? false : booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);
		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

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
		Passenger passenger;

		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.bookingRepository.findBookingById(bookingId);

		Date moment;
		moment = MomentHelper.getCurrentMoment();

		passenger = new Passenger();
		passenger.setDateOfBirth(moment);
		passenger.setCustomer(booking.getCustomer());
		passenger.setDraftMode(true);
		super.getBuffer().addData(passenger);

		this.bookingRecord.setBooking(booking);

	}

	@Override
	public void bind(final Passenger passenger) {

		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");

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

		super.addPayload(dataset, passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);

	}
}
