
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
	private CustomersBookingRepository	repository;

	// AbstractGuiService interface -------------------------------------------
	Collection<Flight>					vuelos;


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
	public void bind(final Booking booking) {
		int flightId = super.getRequest().getData("vuelo", int.class);

		Flight flight = this.repository.findFlightById(flightId);
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble");
		booking.setFlight(flight);
		booking.setDraftMode(true);

	}

	@Override
	public void validate(final Booking booking) {
		String cod = booking.getLocatorCode();
		Collection<Booking> codigo = this.repository.findAllBookingLocatorCode(cod);
		Date d = booking.getPurchaseMoment() == null ? null : booking.getPurchaseMoment();
		if (!codigo.isEmpty())
			super.state(false, "locatorCode", "customers.booking.error.repeat-code");
		if (booking.getFlight() == null)
			super.state(false, "vuelo", "customers.booking.error.no-flight");
		else if (d == null)
			super.state(false, "purchaseMoment", "customers.booking.error.moment");
		else if (!booking.getFlight().getScheduledDeparture().after(d))
			super.state(false, "vuelo", "customers.booking.error.cannotChoseFlight");

	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices flightChoices;
		Collection<Flight> vuelos;

		vuelos = this.vuelosFiltrados(booking);
		Flight flight = booking.getFlight();
		if (flight != null && !vuelos.contains(flight))
			flight = null;

		flightChoices = SelectChoices.from(vuelos, "tag", flight);
		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode");
		dataset.put("vuelo", flightChoices.getSelected() != null ? flightChoices.getSelected().getKey() : "");
		dataset.put("vuelos", flightChoices);
		dataset.put("price", booking.getPrice());
		dataset.put("travelClasses", choices);

		super.getResponse().addData(dataset);

	}

	public Collection<Flight> vuelosFiltrados(final Booking booking) {
		if (booking.getPurchaseMoment() != null) {
			Collection<Flight> vuelosPublicados = this.repository.getAllFlight();
			return vuelosPublicados.stream().filter(x -> x.getScheduledDeparture().after(booking.getPurchaseMoment())).toList();
		} else
			return this.repository.getAllFlight();

	}
}
