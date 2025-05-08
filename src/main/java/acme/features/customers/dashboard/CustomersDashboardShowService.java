
package acme.features.customers.dashboard;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Statistics;
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
		Date moment;
		moment = MomentHelper.getCurrentMoment();
		LocalDate localDateMoment = moment.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dateLimit = localDateMoment.minusYears(1);

		LocalDate dateLimit2 = localDateMoment.minusYears(5);

		Date date = Date.from(dateLimit.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date date2 = Date.from(dateLimit2.atStartOfDay(ZoneId.systemDefault()).toInstant());

		//5 ultimos destinos;
		List<String> theLastFiveDestinations;

		//money during the last year;
		Double moneySpentInBookingDuringLastYear;

		//number of booking grouped
		Map<TravelClass, Integer> bookingsGroupedByTravelClass;

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
		theLastFiveDestinations = this.repository.find5topFlightsByCustomerId(customer.getId());
		moneySpentInBookingDuringLastYear = this.repository.moneySpentInBookingDuringLastYear(id, date);
		bookingsGroupedByTravelClass = this.repository.totalTypes(id);

		countPassenger = this.repository.countPassengersByCustomer(id) != null ? this.repository.countPassengersByCustomer(id) : 0;
		averagePassenger = this.repository.averagePassengersByCustomer(id) != null ? this.repository.averagePassengersByCustomer(id) : 0;
		minPassenger = this.repository.minPassengersByCustomer(id) != null ? this.repository.minPassengersByCustomer(id) : 0.0;
		maxPassenger = this.repository.maxPassengersByCustomer(id) != null ? this.repository.maxPassengersByCustomer(id) : 0.0;

		stddevPassenger = this.calculateDeviation(id) != null ? this.calculateDeviation(id) : 0.0;
		countBooking = this.repository.countBookingsInLastFiveYears(customer, date2) != null ? this.repository.countBookingsInLastFiveYears(customer, date2) : 0;
		averageBooking = this.repository.averageBookingCostInLastFiveYears(customer, date2) != null ? this.repository.averageBookingCostInLastFiveYears(customer, date2) : 0;
		minBooking = this.repository.minBookingCostInLastFiveYears(customer, date2) != null ? this.repository.minBookingCostInLastFiveYears(customer, date2) : 0.0;
		maxBooking = this.repository.maxBookingCostInLastFiveYears(customer, date2) != null ? this.repository.maxBookingCostInLastFiveYears(customer, date2) : 0.0;
		stddevBooking = this.repository.deviationBookingCost(customer, date2) != null ? this.repository.stddevBookingCostInLastFiveYears(customer, date2) : 0.0;

		Statistics statPassenger = new Statistics();
		statPassenger.setCount(countPassenger);
		statPassenger.setAverage(averagePassenger);
		statPassenger.setMax(maxPassenger);
		statPassenger.setMin(minPassenger);
		statPassenger.setDeviation(stddevPassenger);

		Statistics statBooking = new Statistics();
		statBooking.setCount(countBooking);
		statBooking.setAverage(averageBooking);
		statBooking.setMax(maxBooking);
		statBooking.setMin(minBooking);
		statBooking.setDeviation(stddevBooking);

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
		Integer totalNumBUSINESS;
		Integer totalNumEconomy;
		String res5LastCity = String.join(" ", object.getTheLastFiveDestinations());
		totalNumBUSINESS = object.getBookingsGroupedByTravelClass().get(TravelClass.BUSINESS) != null ? object.getBookingsGroupedByTravelClass().get(TravelClass.BUSINESS) : 0;
		totalNumEconomy = object.getBookingsGroupedByTravelClass().get(TravelClass.ECONOMY) != null ? object.getBookingsGroupedByTravelClass().get(TravelClass.ECONOMY) : 0;

		dataset = super.unbindObject(object, //
			"moneySpentInBookingDuringLastYear",// 
			"booking5Years", "passengersBooking");
		dataset.put("totalNumTravelClassEconomy", totalNumEconomy);
		dataset.put("totalNumTravelClassBusiness", totalNumBUSINESS);
		dataset.put("theLastFiveDestinations", res5LastCity);

		super.getResponse().addData(dataset);
	}

	public Double calculateDeviation(@Param("customer") final int customer) {

		Integer totalPassengers = this.repository.countPassengersByCustomer(customer);

		Double averagePassengers = this.repository.averagePassengersByCustomer(customer);

		Double minPassengers = this.repository.minPassengersByCustomer(customer);

		Double maxPassengers = this.repository.maxPassengersByCustomer(customer);

		double variance = 0.0;

		for (int i = minPassengers.intValue(); i <= maxPassengers.intValue(); i++) {
			Double diff = i - averagePassengers;
			variance += Math.pow(diff, 2);
		}

		Double deviation = Math.sqrt(variance / totalPassengers);

		return deviation;
	}

}
