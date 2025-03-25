
package acme.features.administrator.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;

@GuiService
public class AdministratorBookingShowService extends AbstractGuiService<Administrator, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {

		Dataset dataset;
		Money price = booking.getPrice();

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draft-mode");
		dataset.put("price", price);

		super.getResponse().addData(dataset);
	}
}
