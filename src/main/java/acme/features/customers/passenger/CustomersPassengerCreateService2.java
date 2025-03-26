
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

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Customers customer;

		customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();
		status = super.getRequest().getPrincipal().hasRealm(customer);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		Customers customer;

		customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();
		Date moment;
		moment = MomentHelper.getCurrentMoment();

		passenger = new Passenger();
		passenger.setDateOfBirth(moment);
		passenger.setCustomer(customer);
		System.out.println("llega hasta a qui?");

		super.getBuffer().addData(passenger);
		System.out.println("hellohellohello");

	}

	@Override
	public void bind(final Passenger passenger) {

		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
		passenger.setDraftMode(true);
		System.out.println("hellohellohello3");

	}

	@Override
	public void validate(final Passenger passenger) {

		;
	}

	@Override
	public void perform(final Passenger passenger) {
		System.out.println("se guarda? pasenger");
		this.repository.save(passenger);

		System.out.println("si");
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.bookingRepository.findBookingById(bookingId);
		System.out.println(booking.getLocatorCode());
		if (booking != null) {
			System.out.println("se duarga bookingrecord");
			BookingRecord bookingRecord = new BookingRecord();
			bookingRecord.setBooking(booking);
			bookingRecord.setPassenger(passenger);
			this.bookingRepository.save(bookingRecord);
			System.out.println("pls si");
		}

	}

	@Override
	public void unbind(final Passenger passenger) {

		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);
		System.out.println("hello hello");

	}
}
