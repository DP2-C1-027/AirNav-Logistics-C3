
package acme.features.customers.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.components.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.Passenger;
import acme.entities.flights.Flight;
import acme.realms.Customers;

@Repository
public interface CustomersBookingRepository extends AbstractRepository {

	//listar los booking de un customer

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findByCustomer(@Param("customerId") final int customerId);

	//show details and passengers

	@Query("SELECT b from Booking b where b.id =:id")
	Booking findBookinById(final int id);

	@Query("select b.passenger from BookingRecord b  WHERE b.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBookingId(@Param("bookingId") int bookingId);

	//necesito el vuelo asociado a la reserva para saber si existe el vuelo o no

	@Query("SELECT b.flight FROM Booking b WHERE b.id=:bookingId")
	Flight findFlightByBookingId(@Param("bookingId") int bookingId);

	@Query("SELECT c FROM Customers c WHERE c.id = :customerId")
	Customers findCustomer(@Param("customerId") int customerId);

	//@Query("SELECT f FROM Flight f WHERE f.departureTime > CURRENT_TIMESTAMP")
	//Collection<Flight> findAvailableFlights();

	@Query("SELECT f.cost FROM Flight f WHERE f.id=:id")
	Money getPriceById(final int id);

	@Query("SELECT f FROM Flight f")
	Collection<Flight> getAllFlight();

	@Query("SELECT f FROM Flight f WHERE f.id = ?1")
	Flight findFlightById(int id);

	//create and update

}
