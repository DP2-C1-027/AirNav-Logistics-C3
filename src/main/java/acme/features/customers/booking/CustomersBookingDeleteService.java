
package acme.features.customers.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.TravelClass;
import acme.features.customers.bookingRecord.CustomersBookingRecordRepository;
import acme.realms.Customers;

@GuiService
public class CustomersBookingDeleteService extends AbstractGuiService<Customers, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRepository			repository;

	@Autowired
	private CustomersBookingRecordRepository	brRepo;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status = true;

		Booking booking;
		Customers customer;
		if (super.getRequest().hasData("id", int.class)) {
			Integer bookingId;
			try {
				bookingId = super.getRequest().getData("id", int.class);
			} catch (Exception e) {
				bookingId = null;
			}
			booking = bookingId != null ? this.repository.findBookinById(bookingId) : null;
			customer = booking != null ? booking.getCustomer() : null;

			status = customer == null ? false : booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);
		}

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

		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(booking.isDraftMode(), "draftMode", "customers.form.error.draft-mode.delete");

	}

	@Override
	public void perform(final Booking booking) {
		Collection<BookingRecord> br;

		br = this.repository.findAllBookingRecordById(booking.getId());
		this.brRepo.deleteAll(br);
		this.repository.delete(booking);

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
		super.getResponse().addData(dataset);
	}
}
