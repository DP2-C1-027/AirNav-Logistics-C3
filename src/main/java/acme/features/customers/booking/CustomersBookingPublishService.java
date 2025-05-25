
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
import acme.entities.flights.Flight;
import acme.realms.Customers;

@GuiService
public class CustomersBookingPublishService extends AbstractGuiService<Customers, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		Booking booking;
		Customers customer;
		if (super.getRequest().hasData("id")) {
			Integer bookingId;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				bookingId = Integer.valueOf(isInteger);
			else
				bookingId = Integer.valueOf(-1);
			booking = bookingId.equals(Integer.valueOf(-1)) ? null : this.repository.findBookinById(bookingId);
			customer = booking == null ? null : booking.getCustomer();
			status = customer == null ? false : booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);
		} else
			status = false;

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

		super.bindObject(booking, "locatorCode", "travelClass", "lastNibble");

	}

	@Override
	public void validate(final Booking booking) {
		boolean isValidNibble = !booking.getLastNibble().isEmpty();

		super.state(isValidNibble, "lastNibble", "customer.booking.error.nibble-required");
		String cod = booking.getLocatorCode();
		Collection<Booking> codigo = this.repository.findAllBookingLocatorCodeUpdate(cod, booking.getId());

		if (!codigo.isEmpty())
			super.state(false, "locatorCode", "customers.booking.error.repeat-code");

		Collection<Passenger> p = this.repository.findPassengersByBookingId(booking.getId());

		boolean confirmation = p.stream().allMatch(x -> !x.isDraftMode());
		if (p.isEmpty())
			super.state(false, "*", "customer.booking.error.noPassenger.message");

		super.state(confirmation, "*", "customer.booking.error.unpublishedPassengers.message");
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

		Integer numero = this.repository.getNumberofPassenger(booking.getId());
		double precio = booking.getPrice().getAmount() * numero;
		String moneda = booking.getPrice().getCurrency();
		Money precioNuevo = new Money();
		precioNuevo.setAmount(precio);
		precioNuevo.setCurrency(moneda);

		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode");
		dataset.put("price", precioNuevo);
		dataset.put("travelClasses", choices);
		Flight f = booking.getFlight();
		dataset.put("flight", f.getTag() + " : " + f.getDepartureCity() + "->" + f.getArrivalCity());
		super.addPayload(dataset, booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode", "price", "flight");
		super.getResponse().addData(dataset);
	}

}
