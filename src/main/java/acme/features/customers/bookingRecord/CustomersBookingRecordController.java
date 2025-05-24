
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
	private CustomersBookingRecordCreateService	createService;

	@Autowired
	private CustomersBookingRecordDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);

		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}
}
