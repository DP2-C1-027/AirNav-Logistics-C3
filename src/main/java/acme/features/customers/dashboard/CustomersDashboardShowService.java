
package acme.features.customers.dashboard;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
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
		LocalDate dateLimit = LocalDate.now().minusYears(1);
		LocalDate dateLimit2 = LocalDate.now().minusYears(5);

		Date date = Date.from(dateLimit.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date date2 = Date.from(dateLimit2.atStartOfDay(ZoneId.systemDefault()).toInstant());

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
		moneySpentInBookingDuringLastYear = this.repository.moneySpentInBookingDuringLastYear(id, date);
		bookingsGroupedByTravelClass = this.repository.totalTypes(id);
		countPassenger = this.repository.countPassengersByCustomer(id) != null ? this.repository.countPassengersByCustomer(id) : 0;
		averagePassenger = this.repository.averagePassengersByCustomer(id) != null ? this.repository.averagePassengersByCustomer(id) : 0;
		minPassenger = this.repository.minPassengersByCustomer(id) != null ? this.repository.minPassengersByCustomer(id) : 0.0;
		maxPassenger = this.repository.maxPassengersByCustomer(id) != null ? this.repository.maxPassengersByCustomer(id) : 0.0;
		//stddevPassenger = this.repository.stddevPassengersByCustomer(customer) != null ? this.repository.stddevPassengersByCustomer(customer) : 0.0;
		//countBooking = this.repository.countBookingsInLastFiveYears(customer, date2) != null ? this.repository.countBookingsInLastFiveYears(customer, date2) : 0;
		//	averageBooking = this.repository.averageBookingCostInLastFiveYears(customer, date2) != null ? this.repository.averageBookingCostInLastFiveYears(customer, date2) : 0;
		//	minBooking = this.repository.minBookingCostInLastFiveYears(customer, date2) != null ? this.repository.minBookingCostInLastFiveYears(customer, date2) : 0.0;
		//	maxBooking = this.repository.maxBookingCostInLastFiveYears(customer, date2) != null ? this.repository.maxBookingCostInLastFiveYears(customer, date2) : 0.0;
		//	stddevBooking = this.repository.stddevBookingCostInLastFiveYears(customer, date2) != null ? this.repository.stddevBookingCostInLastFiveYears(customer, date2) : 0.0;

		Statistics statPassenger = new Statistics();
		statPassenger.setCount(countPassenger);
		statPassenger.setAverage(averagePassenger);
		statPassenger.setMax(maxPassenger);
		statPassenger.setMin(minPassenger);
		//	statPassenger.setDeviationsss(stddevPassenger);

		Statistics statBooking = new Statistics();
		//statBooking.setCount(countBooking);
		//statBooking.setAverage(averageBooking);
		//	statBooking.setMax(maxBooking);
		//statBooking.setMin(minBooking);
		//	statBooking.setDeviationsss(stddevBooking);

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
