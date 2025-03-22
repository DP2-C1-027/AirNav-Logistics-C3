
package acme.features.customers.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.booking.Booking;
import acme.realms.Customers;

@GuiController
public class CustomersBookingController extends AbstractGuiController<Customers, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersBookingListService		listService;

	@Autowired
	private CustomersBookingShowService		showService;

	@Autowired
	private CustomersBookingCreateService	createService;

	@Autowired
	private CustomersBookingUpdateService	updateService;

	@Autowired
	private CustomersBookingPublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);

	}
}
