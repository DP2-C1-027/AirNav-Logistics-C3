
package acme.features.customers.dashboard;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customers;

@Repository
public interface CustomersDashboardRepository extends AbstractRepository {

	@Query("select c from Customers c where c.id=:id")
	Customers findCustomer(int id);

	@Query("select f from Flight f where f.id=:id")
	Flight findById(int id);
	//last five destinations
	@Query("""
		    SELECT distinct b.flight
		    FROM Booking b
		    WHERE b.customer.id = :customerId AND b.flight.draftMode=false
		""")
	List<Flight> findFlightsByCustomerId(@Param("customerId") int customerId);

	default List<String> find5topFlightsByCustomerId(final int customerId) {
		List<Flight> reservas = this.findFlightsByCustomerId(customerId);
		List<String> vuelosOrdenados = reservas.stream().sorted(Comparator.comparing(Flight::getScheduledDeparture).reversed())  // Ordena en orden descendente por la fecha
			.map(x -> x.getArrivalCity()).distinct().limit(5)  // Limita a los 5 vuelos más recientes
			.collect(Collectors.toList());
		return vuelosOrdenados;// Ordena en orden descendente por la fecha de salida programada

	}
	//•	The money spent in bookings during the last year
	@Query("SELECT SUM(b.flight.cost.amount*(SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b)) FROM Booking b WHERE b.customer.id = :customerId AND b.purchaseMoment >= :dateLimit")
	Double moneySpentInBookingDuringLastYear(@Param("customerId") int customerId, @Param("dateLimit") Date dateLimit);

	//•	Their number of bookings grouped by travel class

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
	@Query("SELECT COUNT(br) FROM BookingRecord br WHERE br.passenger.customer.id = :customer")
	Integer countPassengersByCustomer(@Param("customer") int customer);

	@Query("SELECT AVG(select count(b) from BookingRecord b where b.booking.id=br.id) FROM Booking br WHERE br.customer.id = :customer ")
	Double averagePassengersByCustomer(@Param("customer") int customer);

	@Query("SELECT MIN(select count(b) from BookingRecord b where b.booking.id=br.id) FROM Booking br WHERE br.customer.id = :customer ")
	Double minPassengersByCustomer(@Param("customer") int customer);

	@Query("SELECT MAX(select count(b) from BookingRecord b where b.booking.id=br.id) FROM Booking br WHERE br.customer.id = :customer  ")
	Double maxPassengersByCustomer(@Param("customer") int customer);

	//estadistica booking
	@Query("SELECT COUNT(b) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= :dateLimit")
	Integer countBookingsInLastFiveYears(@Param("customer") Customers customer, @Param("dateLimit") Date dateLimit);

	@Query("SELECT AVG(b.flight.cost.amount * (SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b)) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= :dateLimit")
	Double averageBookingCostInLastFiveYears(@Param("customer") Customers customer, @Param("dateLimit") Date dateLimit);

	@Query("SELECT MIN(b.flight.cost.amount* (SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b)) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= :dateLimit")
	Double minBookingCostInLastFiveYears(@Param("customer") Customers customer, @Param("dateLimit") Date dateLimit);

	@Query("SELECT MAX(b.flight.cost.amount*(SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b)) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= :dateLimit")
	Double maxBookingCostInLastFiveYears(@Param("customer") Customers customer, @Param("dateLimit") Date dateLimit);

	@Query("SELECT STDDEV(b.flight.cost.amount) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= :dateLimit")
	Double stddevBookingCostInLastFiveYears(@Param("customer") Customers customer, @Param("dateLimit") Date dateLimit);

	@Query("SELECT SQRT(SUM((b.flight.cost.amount*(SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b) - (SELECT AVG(b2.flight.cost.amount*(SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b)) FROM Booking b2 WHERE b2.customer = :customer AND b2.purchaseMoment >= :moment)) * (b.flight.cost.amount*(SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b) - (SELECT AVG(b2.flight.cost.amount*(SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b)) FROM Booking b2 WHERE b2.customer = :customer AND b2.purchaseMoment >= :moment))) / COUNT(b)) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= :moment")
	Double deviationBookingCost(@Param("customer") Customers customer, @Param("moment") Date moment);

}
