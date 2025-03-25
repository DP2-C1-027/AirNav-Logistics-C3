
package acme.features.customers.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customers;

@GuiService
public class CustomersBookingCreateService extends AbstractGuiService<Customers, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRepository repository;

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
		Date moment;
		Customers customer;
		Booking whine;

		moment = MomentHelper.getCurrentMoment();
		customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();

		whine = new Booking();

		whine.setPurchaseMoment(moment);
		whine.setCustomer(customer);

		super.getBuffer().addData(whine);

	}

	@Override
	public void bind(final Booking whine) {
		int flightId = super.getRequest().getData("vuelo", int.class);

		Flight flight = this.repository.findFlightById(flightId);
		super.bindObject(whine, "locatorCode", "purchaseMoment", "travelClass", "lastNibble");
		whine.setFlight(flight);
		whine.setDraftMode(true);

	}

	@Override
	public void validate(final Booking whine) {
		String cod = whine.getLocatorCode();
		Collection<Booking> codigo = this.repository.findAllBookingLocatorCode(cod);
		if (!codigo.isEmpty())
			super.state(false, "locatorCode", "customers.booking.error.repeat-code");
		if (whine.getFlight() == null)
			super.state(false, "vuelo", "customers.booking.error.no-flight");
	}

	@Override
	public void perform(final Booking whine) {
		this.repository.save(whine);
	}

	@Override
	public void unbind(final Booking whine) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices flightChoices;
		Collection<Flight> vuelos;
		vuelos = this.repository.getAllFlight();
		flightChoices = SelectChoices.from(vuelos, "tag", whine.getFlight());
		choices = SelectChoices.from(TravelClass.class, whine.getTravelClass());
		dataset = super.unbindObject(whine, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draft-mode");
		dataset.put("vuelo", flightChoices.getSelected().getKey());
		dataset.put("vuelos", flightChoices);
		dataset.put("price", whine.getPrice());
		dataset.put("travelClasses", choices);

		super.getResponse().addData(dataset);

	}
}
