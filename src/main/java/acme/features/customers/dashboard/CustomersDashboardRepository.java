
package acme.features.customers.dashboard;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.realms.Customers;

@Repository
public interface CustomersDashboardRepository extends AbstractRepository {

	@Query("select c from Customers c where c.id=:id")
	Customers findCustomer(int id);

	//last five destinations
	@Query("SELECT DISTINCT l.arrivalAirport.city FROM Booking b JOIN b.flight f JOIN Leg l ON l.flight = f WHERE b.customer.id = :customerId ORDER BY l.scheduledArrival DESC")
	List<String> findLastFiveDestinations(int customerId);

	//•	The money spent in bookings during the last year
	@Query("select sum(b.flight.cost.amount) From Booking b where b.customer.id=:customerId AND b.purchaseMoment>=FUNCTION('DATEADD', 'YEAR', -1, CURRENT_DATE)")
	Double moneySpentInBookingDuringLastYear(int customerId);

	//•	Their number of bookings grouped by travel class
	@Query("SELECT b.travelClass, COUNT(b) FROM Booking b WHERE b.customer.id=:customerId GROUP BY b.travelClass")
	Map<TravelClass, Integer> bookingsGroupedByTravelClass(int customerId);

	@Query("select c from Booking c where c.customer.id = :auditorId")
	Collection<Booking> findPublishedCodeAudits(int auditorId);

	default Map<TravelClass, Integer> totalTypes(final int customerId) {
		Map<TravelClass, Integer> result = new EnumMap<>(TravelClass.class);
		Collection<Booking> codeAudits = this.findPublishedCodeAudits(customerId);

		for (TravelClass type : TravelClass.values())
			result.put(type, 0);
		for (Booking c : codeAudits)
			result.put(c.getTravelClass(), result.get(c.getTravelClass()) + 1);
		return result;
	}

	//estadistica de passenger in ther booking
	//count of number of passegers in their booking
	@Query("SELECT COUNT(br) FROM BookingRecord br WHERE br.booking.customer = :customer")
	Integer countPassengersByCustomer(@Param("customer") Customers customer);

	@Query("SELECT AVG(COUNT(br)) FROM BookingRecord br WHERE br.booking.customer = :customer")
	Double averagePassengersByCustomer(@Param("customer") Customers customer);

	@Query("SELECT MIN(COUNT(br)) FROM BookingRecord br WHERE br.booking.customer = :customer")
	Long minPassengersByCustomer(@Param("customer") Customers customer);

	@Query("SELECT MAX(COUNT(br)) FROM BookingRecord br WHERE br.booking.customer = :customer")
	Long maxPassengersByCustomer(@Param("customer") Customers customer);

	@Query("SELECT STDDEV(COUNT(br)) FROM BookingRecord br WHERE br.booking.customer = :customer")
	Double stddevPassengersByCustomer(@Param("customer") Customers customer);

	//estadistica booking
	@Query("SELECT COUNT(b.flight.cost.amount) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= FUNCTION('DATEADD', 'YEAR', -5, CURRENT_DATE)")
	Integer countBookingsInLastFiveYears(@Param("customer") Customers customer);

	@Query("SELECT AVG(b.flight.cost.amount) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= FUNCTION('DATEADD', 'YEAR', -5, CURRENT_DATE)")
	Double averageBookingCostInLastFiveYears(@Param("customer") Customers customer);

	@Query("SELECT MIN(b.flight.cost.amount) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= FUNCTION('DATEADD', 'YEAR', -5, CURRENT_DATE)")
	Double minBookingCostInLastFiveYears(@Param("customer") Customers customer);

	@Query("SELECT MAX(b.flight.cost.amount) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= FUNCTION('DATEADD', 'YEAR', -5, CURRENT_DATE)")
	Double maxBookingCostInLastFiveYears(@Param("customer") Customers customer);

	@Query("SELECT STDDEV(b.flight.cost.amount) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= FUNCTION('DATEADD', 'YEAR', -5, CURRENT_DATE)")
	Double stddevBookingCostInLastFiveYears(@Param("customer") Customers customer);

}
