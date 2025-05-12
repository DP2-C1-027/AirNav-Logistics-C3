
package acme.features.customers.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.Passenger;
import acme.features.customers.bookingRecord.CustomersBookingRecordRepository;
import acme.realms.Customers;

@GuiService
public class CustomersPassengerDeleteService extends AbstractGuiService<Customers, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersPassengersRepository		repository;

	@Autowired
	private CustomersBookingRecordRepository	brRepo;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		Passenger passenger;
		Customers customer;
		if (super.getRequest().hasData("id", int.class)) {
			Integer passengerId;
			try {
				passengerId = super.getRequest().getData("id", int.class);
			} catch (Exception e) {
				passengerId = null;
			}

			passenger = passengerId != null ? this.repository.findPassengerById(passengerId) : null;
			customer = passenger != null ? passenger.getCustomer() : null;
			status = customer == null ? false : passenger != null && passenger.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		super.getBuffer().addData(passenger);
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
		Collection<BookingRecord> br;

		br = this.repository.findAllBookingRecordById(passenger.getId());
		this.brRepo.deleteAll(br);
		this.repository.delete(passenger);

	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);
	}
}
