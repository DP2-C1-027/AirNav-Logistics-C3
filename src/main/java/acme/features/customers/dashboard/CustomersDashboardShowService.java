
package acme.features.customers.dashboard;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Statistics;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.forms.CustomersDashboards;
import acme.realms.Customers;

@GuiService
public class CustomersDashboardShowService extends AbstractGuiService<Customers, CustomersDashboards> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomersDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		//super.getResponse().setAuthorised(true);
		//comprobar q es customer
		boolean status;
		Customers customer;

		customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();
		status = super.getRequest().getPrincipal().hasRealm(customer);
		super.getResponse().setAuthorised(status);

	}
	@Override
	public void load() {

		Customers customer = (Customers) super.getRequest().getPrincipal().getActiveRealm();
		CustomersDashboards dashboard;
		int id = customer.getId();

		//5 ultimos destinos;
		List<String> theLastFiveDestinations;

		//money during the last year;
		Double moneySpentInBookingDuringLastYear;

		//number of booking grouped
		Map<TravelClass, Integer> bookingsGroupedByTravelClass;
		Collection<Booking> listaBooking = this.repository.findPublishedCodeAudits(customer.getId());

		//estadistica de passenger:
		Integer countPassenger;
		Double averagePassenger;
		Double minPassenger;
		Double maxPassenger;
		Double stddevPassenger;

		//estadistica booking:
		Integer countBooking;
		Double averageBooking;
		Double minBooking;
		Double maxBooking;
		Double stddevBooking;

		//asigamos:
		theLastFiveDestinations = this.repository.findLastFiveDestinations(id);
		moneySpentInBookingDuringLastYear = this.repository.moneySpentInBookingDuringLastYear(id);
		bookingsGroupedByTravelClass = this.repository.totalTypes(id);
		countPassenger = this.repository.countPassengersByCustomer(customer) != null ? this.repository.countPassengersByCustomer(customer) : 0;
		averagePassenger = this.repository.averagePassengersByCustomer(customer) != null ? this.repository.averagePassengersByCustomer(customer) : 0;
		minPassenger = this.repository.minPassengersByCustomer(customer) != null ? this.repository.minPassengersByCustomer(customer) : 0.0;
		maxPassenger = this.repository.maxPassengersByCustomer(customer) != null ? this.repository.maxPassengersByCustomer(customer) : 0.0;
		stddevPassenger = this.repository.stddevPassengersByCustomer(customer) != null ? this.repository.stddevPassengersByCustomer(customer) : 0.0;
		countBooking = this.repository.countBookingsInLastFiveYears(customer) != null ? this.repository.countBookingsInLastFiveYears(customer) : 0;
		averageBooking = this.repository.averageBookingCostInLastFiveYears(customer) != null ? this.repository.averageBookingCostInLastFiveYears(customer) : 0;
		minBooking = this.repository.minBookingCostInLastFiveYears(customer) != null ? this.repository.minBookingCostInLastFiveYears(customer) : 0.0;
		maxBooking = this.repository.maxBookingCostInLastFiveYears(customer) != null ? this.repository.maxBookingCostInLastFiveYears(customer) : 0.0;
		stddevBooking = this.repository.stddevBookingCostInLastFiveYears(customer) != null ? this.repository.stddevBookingCostInLastFiveYears(customer) : 0.0;

		Statistics statPassenger = new Statistics();
		statPassenger.setCount(countPassenger);
		statPassenger.setAverage(averagePassenger);
		statPassenger.setMax(maxPassenger);
		statPassenger.setMin(minPassenger);
		statPassenger.setDeviationsss(stddevPassenger);

		Statistics statBooking = new Statistics();
		statBooking.setCount(countBooking);
		statBooking.setAverage(averageBooking);
		statBooking.setMax(maxBooking);
		statBooking.setMin(minBooking);
		statBooking.setDeviationsss(stddevBooking);

		dashboard = new CustomersDashboards();
		dashboard.setTheLastFiveDestinations(theLastFiveDestinations);
		dashboard.setMoneySpentInBookingDuringLastYear(moneySpentInBookingDuringLastYear);
		dashboard.setBookingsGroupedByTravelClass(bookingsGroupedByTravelClass);
		dashboard.setBooking5Years(statBooking);
		dashboard.setPassengersBooking(statPassenger);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomersDashboards object) {
		Dataset dataset;

		dataset = super.unbindObject(object, //
			"theLastFiveDestinations", "moneySpentInBookingDuringLastYear", // 
			"bookingsGroupedByTravelClass", "booking5Years", "passengersBooking");

		super.getResponse().addData(dataset);
	}

}
