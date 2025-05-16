
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
		Customers customer;
		Flight f;
		customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();

		if (super.getRequest().hasData("travelClass")) {
			TravelClass valor;
			try {
				valor = super.getRequest().getData("travelClass", TravelClass.class);

			} catch (Exception e) {
				status = false;

			}
		}

		if (super.getRequest().hasData("flight")) {
			Integer flightId;
			try {
				flightId = super.getRequest().getData("flight", Integer.class);

				if (!flightId.equals(Integer.valueOf(0))) {
					f = this.repository.findFlightById(flightId);
					Date d = f.getScheduledDeparture();
					Date moment = super.getRequest().getData("purchaseMoment", Date.class);
					status = super.getRequest().getPrincipal().hasRealm(customer) && !f.isDraftMode() && d.after(moment);

				}
			} catch (Exception e) {
				status = false;

			}

		}

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
		//Date d = booking == null ? null : booking.getPurchaseMoment();

		if (!codigo.isEmpty())
			super.state(false, "locatorCode", "customers.booking.error.repeat-code");
		if (booking.getFlight() == null)
			super.state(false, "flight", "customers.booking.error.no-flight");
		//if (d == null)
		//super.state(false, "purchaseMoment", "customers.booking.error.moment");
		//if (booking.getFlight() != null)
		//if (booking.getFlight().isDraftMode())
		//super.state(false, "flight", "customers.booking.error.vuelos-draftmode");
		//else if (!booking.getFlight().getScheduledDeparture().after(d))
		//super.state(false, "flight", "customers.booking.error.cannotChoseFlight");

	}

	@Override
	public void perform(final Booking booking) {
		assert booking != null;
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;

		Collection<Flight> vuelos;

		vuelos = this.vuelosFiltrados(booking);
		Flight flight = booking.getFlight();
		if (flight != null && !vuelos.contains(flight))
			flight = null;
		SelectChoices flightChoices2 = new SelectChoices();
		flightChoices2.add("0", "----", flight == null); // opción vacía por defecto
		for (Flight f : vuelos) {
			String label = f.getTag() + " : " + f.getDepartureCity() + "->" + f.getArrivalCity();
			String key = Integer.toString(f.getId());
			boolean isSelected = flight != null && f.equals(flight);
			flightChoices2.add(key, label, isSelected);
		}

		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode");
		dataset.put("flight", flightChoices2.getSelected() != null ? flightChoices2.getSelected().getKey() : "");
		dataset.put("vuelos", flightChoices2);
		dataset.put("price", booking.getPrice());
		dataset.put("travelClasses", choices);

		super.addPayload(dataset, booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode", "flight", "price");

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
