
package acme.features.customers.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.Passenger;
import acme.entities.booking.TravelClass;
import acme.realms.Customers;

@GuiService
public class CustomersBookingPublishService extends AbstractGuiService<Customers, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		Customers customer;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookinById(bookingId);
		customer = booking == null ? null : booking.getCustomer();
		status = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookinById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {

		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble");

	}

	@Override
	public void validate(final Booking booking) {
		boolean isValidNibble = booking.getLastNibble() != null && !booking.getLastNibble().isEmpty();

		super.state(isValidNibble, "lastNibble", "customer.booking.error.nibble-required");

		Collection<Passenger> p = this.repository.findPassengersByBookingId(booking.getId());

		boolean confirmation = p.isEmpty() || p.stream().allMatch(x -> !x.isDraftMode());

		//confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "lastNibble", "customer.booking.error.unpublishedPassengers.message");
	}
	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {

		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode");
		Money price = booking.getPrice();
		dataset.put("price", price);

		dataset.put("travelClasses", choices);
		super.getResponse().addData(dataset);
	}
}
