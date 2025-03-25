
package acme.features.customers.bookingRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.booking.BookingRecord;
import acme.realms.Customers;

@GuiController
public class CustomersBookingRecordController extends AbstractGuiController<Customers, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingRecordListService	listService;

	@Autowired
	private CustomersBookingRecordShowService	showService;

	@Autowired
	private CustomersBookingRecordUpdateService	updateService;

	@Autowired
	private CustomersBookingRecordCreateService	createService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("create", this.createService);
	}
}
