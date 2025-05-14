
package acme.features.customers.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.Customers;

@GuiService
public class CustomersBookingListService extends AbstractGuiService<Customers, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {
		Collection<Booking> booking;
		int customerId;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		booking = this.repository.findByCustomer(customerId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		Integer numero = this.repository.getNumberofPassenger(booking.getId());
		double precio = booking.getPrice().getAmount() * numero;
		String moneda = booking.getPrice().getCurrency();
		Money precioNuevo = new Money();
		precioNuevo.setAmount(precio);
		precioNuevo.setCurrency(moneda);
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble");
		dataset.put("price", precioNuevo);
		super.addPayload(dataset, booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "price");
		super.getResponse().addData(dataset);

	}

}
