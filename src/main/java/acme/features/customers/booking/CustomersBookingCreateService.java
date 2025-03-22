
package acme.features.customers.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.flights.Flight;
import acme.realms.Customers;

@GuiService
public class CustomersBookingCreateService extends AbstractGuiService<Customers, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	//deberia comprobar q existe el vuelo???
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
		Collection<Flight> availableFlights;

		moment = MomentHelper.getCurrentMoment();
		customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();

		whine = new Booking();

		whine.setPurchaseMoment(moment);
		whine.setCustomer(customer);
		whine.setLocatorCode("");
		whine.setTravelClass(null);
		whine.setLastNibble(null);
		super.getBuffer().addData(whine);

	}

	@Override
	public void bind(final Booking whine) {
		super.bindObject(whine, "locatorCode", "purchaseMoment", "travelClass", "lastNibble");

		// Enlazar el vuelo seleccionado

	}

	@Override
	public void validate(final Booking whine) {
		//boolean confirmation;

		//confirmation = super.getRequest().getData("confirmation", boolean.class);
		//super.state(confirmation, "confirmation", "acme.validation.confirmation.message");

		// Verifica que el vuelo seleccionado sea válido
		//super.state(whine.getFlight() != null, "flight", "acme.validation.flight.missing");

		// Verifica que la clase de viaje sea válida
		//super.state(whine.getTravelClass() != null, "travelClass", "acme.validation.travelClass.invalid");
	}

	@Override
	public void perform(final Booking whine) {
		this.repository.save(whine);
	}

	@Override
	public void unbind(final Booking whine) {
		Dataset dataset;

		dataset = super.unbindObject(whine, "locatorCode", "purchaseMoment", "travelClass", "lastNibble");
		dataset.put("price", whine.getPrice());

		super.getResponse().addData(dataset);

	}
}
