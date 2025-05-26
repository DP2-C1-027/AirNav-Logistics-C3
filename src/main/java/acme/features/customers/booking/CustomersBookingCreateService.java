
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
		boolean status = true;
		//Customers customer;
		Flight flight;
		//customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();

		if (super.getRequest().hasData("id")) {
			Integer id;

			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				id = Integer.valueOf(isInteger);
			else
				id = Integer.valueOf(-1);
			if (!id.equals(Integer.valueOf(0)))
				status = false;

		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

		if (!status) {
			super.getResponse().setAuthorised(false);
			return;
		}

		if (super.getRequest().hasData("flight")) {
			Integer flightId;
			String isInteger;

			isInteger = super.getRequest().getData("flight", String.class);
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				flightId = Integer.valueOf(isInteger);
			else
				flightId = Integer.valueOf(-1);

			if (!flightId.equals(Integer.valueOf(0))) {
				flight = this.repository.findFlightById(flightId);
				if (flight == null)
					status = false;
				else {
					Date d = flight.getScheduledDeparture();
					Date moment = super.getRequest().getData("purchaseMoment", Date.class);
					status = !flight.isDraftMode() && d.after(moment);

				}

			}

		} else if (super.getRequest().getMethod().equals("POST"))
			status = false;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Date moment;
		Customers customer;
		Booking booking;

		moment = MomentHelper.getCurrentMoment();
		customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();

		booking = new Booking();

		booking.setPurchaseMoment(moment);

		booking.setCustomer(customer);
		booking.setDraftMode(true);

		super.getBuffer().addData(booking);

	}

	@Override
	public void bind(final Booking booking) {
		int flightId = super.getRequest().getData("flight", int.class);

		Flight flight = this.repository.findFlightById(flightId);
		super.bindObject(booking, "locatorCode", "travelClass", "lastNibble");
		booking.setFlight(flight);

	}

	@Override
	public void validate(final Booking booking) {

		String cod = booking.getLocatorCode();

		Collection<Booking> codigo = this.repository.findAllBookingLocatorCode(cod);

		if (!codigo.isEmpty())
			super.state(false, "locatorCode", "customers.booking.error.repeat-code");
		if (booking.getFlight() == null)
			super.state(false, "flight", "customers.booking.error.no-flight");

	}

	@Override
	public void perform(final Booking booking) {

		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;

		Collection<Flight> vuelosDisponibles;

		vuelosDisponibles = this.vuelosFiltrados(booking);
		Flight flight = booking.getFlight();
		//if (flight != null && !vuelosDisponibles.contains(flight))
		//	flight = null;
		SelectChoices flightChoices2 = new SelectChoices();
		flightChoices2.add("0", "----", flight == null); // opción vacía por defecto
		for (Flight f : vuelosDisponibles) {
			String label = f.getTag() + " : " + f.getDepartureCity() + "->" + f.getArrivalCity();
			String key = Integer.toString(f.getId());
			boolean isSelected = flight != null && f.equals(flight);
			flightChoices2.add(key, label, isSelected);
		}

		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode");
		dataset.put("flight", flightChoices2.getSelected().getKey());
		dataset.put("vuelos", flightChoices2);
		dataset.put("price", booking.getPrice());
		dataset.put("travelClasses", choices);

		super.addPayload(dataset, booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode", "flight", "price");

		super.getResponse().addData(dataset);

	}

	public Collection<Flight> vuelosFiltrados(final Booking booking) {

		Collection<Flight> vuelosPublicados = this.repository.getAllFlight();
		return vuelosPublicados.stream().filter(x -> x.getScheduledDeparture().after(booking.getPurchaseMoment())).toList();

	}
}
