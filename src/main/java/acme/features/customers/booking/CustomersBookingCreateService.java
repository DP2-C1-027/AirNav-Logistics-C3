
package acme.features.customers.booking;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.Customers;

@GuiService
public class CustomersBookingCreateService extends AbstractGuiService<Customers, Booking> {
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
		Date moment;
		Customers customer;
		Booking whine;

		moment = MomentHelper.getCurrentMoment();
		customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();

		whine = new Booking();
		//whine.setMoment(moment);
		//whine.setHeader("");
		//	whine.setDescription("");
		//whine.setRedress("N/A");
		whine.setCustomer(customer);

		super.getBuffer().addData(whine);
	}

	@Override
	public void bind(final Booking whine) {
		super.bindObject(whine, "header", "description");
	}

	@Override
	public void validate(final Booking whine) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Booking whine) {
		this.repository.save(whine);
	}

	@Override
	public void unbind(final Booking whine) {
		Dataset dataset;

		dataset = super.unbindObject(whine, "header", "description");

		super.getResponse().addData(dataset);

	}
}
