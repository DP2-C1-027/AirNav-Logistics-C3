
package acme.features.customers.dashboard;

import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.TravelClass;
import acme.realms.Customers;

@Repository
public interface CustomersDashboardRepository extends AbstractRepository {

	@Query("select sum(b.flight.cost.amount) From Booking b where b.customer.id=:customerId AND b.purchaseMoment>=CURRENT_DATE-365")
	Double moneySpentInBookingDuringLastYear(int customerId);

	@Query("SELECT b.travelClass, COUNT(b) FROM Booking b WHERE b.customer.id=:customerId AND b.purchaseMoment >= CURRENT_DATE - 365 GROUP BY b.travelClass")
	Map<TravelClass, Integer> bookingsGroupedByTravelClass(int customerId);

	//@Query("SELECT DISTINCT b.flight.getArrivalCity() FROM Booking b ORDER BY b.flight.getScheduledArrival() DESC")
	//List<String> findLastFiveDestinations();
	//como pillo el nombre del destino???

	//estadistica de passenger in ther booking
	@Query("SELECT COUNT(br) FROM BookingRecord br WHERE br.booking.customer = :customer")
	Integer countPassengersByCustomer(@Param("customer") Customers customer);

	@Query("SELECT AVG(COUNT(br)) FROM BookingRecord br WHERE br.booking.customer = :customer GROUP BY br.booking")
	Double averagePassengersByCustomer(@Param("customer") Customers customer);

	@Query("SELECT MIN(COUNT(br)) FROM BookingRecord br WHERE br.booking.customer = :customer GROUP BY br.booking")
	Long minPassengersByCustomer(@Param("customer") Customers customer);

	@Query("SELECT MAX(COUNT(br)) FROM BookingRecord br WHERE br.booking.customer = :customer GROUP BY br.booking")
	Long maxPassengersByCustomer(@Param("customer") Customers customer);

	@Query("SELECT STDDEV(COUNT(br)) FROM BookingRecord br WHERE br.booking.customer = :customer GROUP BY br.booking")
	Double stddevPassengersByCustomer(@Param("customer") Customers customer);

	//estadistica booking
	@Query("SELECT COUNT(b.flight.cost.amount) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= CURRENT_DATE - 5*365")
	Long countBookingsInLastFiveYears(@Param("customer") Customers customer);

	@Query("SELECT AVG(b.flight.cost.amount) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= CURRENT_DATE - 5*365")
	Double averageBookingCostInLastFiveYears(@Param("customer") Customers customer);

	@Query("SELECT MIN(b.flight.cost.amount) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= CURRENT_DATE - 5*365")
	Double minBookingCostInLastFiveYears(@Param("customer") Customers customer);

	@Query("SELECT MAX(b.flight.cost.amount) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= CURRENT_DATE - 5*365")
	Double maxBookingCostInLastFiveYears(@Param("customer") Customers customer);

	@Query("SELECT STDDEV(b.flight.cost.amount) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= CURRENT_DATE - 5*365")
	Double stddevBookingCostInLastFiveYears(@Param("customer") Customers customer);

}
