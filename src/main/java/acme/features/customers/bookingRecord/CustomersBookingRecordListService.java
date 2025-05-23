
package acme.features.customers.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.Passenger;
import acme.entities.flights.Flight;
import acme.realms.Customers;

@GuiService
public class CustomersBookingRecordListService extends AbstractGuiService<Customers, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRecordRepository repository;


	//  ---------------------------
	@Override
	public void authorise() {

		boolean status = true;

		Passenger passenger;

		if (super.getRequest().hasData("passengerId")) {
			Integer id;

			String isInteger;
			isInteger = super.getRequest().getData("passengerId", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				id = Integer.valueOf(isInteger);
			else
				id = Integer.valueOf(-1);

			passenger = id != null ? this.repository.findPassengerById(id) : null;
			Customers customer = passenger != null ? passenger.getCustomer() : null;

			status = customer == null ? false : super.getRequest().getPrincipal().hasRealm(customer);
			//|| passenger != null && !passenger.isDraftMode();
		} else
			status = false;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<BookingRecord> booking;
		int id;
		Passenger passenger;

		id = super.getRequest().getData("passengerId", int.class);
		passenger = this.repository.findPassengerById(id);

		booking = this.repository.findBookingRecordPassenger(passenger.getId(), passenger.getCustomer().getId());

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;

		Booking booking = bookingRecord.getBooking();

		Flight f = booking.getFlight();

		dataset = super.unbindObject(bookingRecord, "booking", "passenger");
		dataset.put("booking", booking.getLocatorCode());
		dataset.put("travelClass", booking.getTravelClass());
		dataset.put("flight", f.getTag() + " : " + f.getDepartureCity() + "->" + f.getArrivalCity());

		super.addPayload(dataset, bookingRecord);

		super.getResponse().addData(dataset);

	}

}
