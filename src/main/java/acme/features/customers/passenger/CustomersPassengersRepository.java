
package acme.features.customers.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.Passenger;
import acme.realms.Customers;

public interface CustomersPassengersRepository extends AbstractRepository {

	//listar todos los passenger de un customer 
	@Query("""
		  SELECT br.passenger
		FROM BookingRecord br
		WHERE br.booking.customer.id = :customerId
		""")

	Collection<Passenger> findPassengersByCustomerId(@Param("customerId") int customerId);

	// detalles de un pasajero:

	@Query("SELECT br.passenger FROM BookingRecord br WHERE br.booking.customer.id = :customerId AND br.passenger.id = :passengerId")
	Passenger findPassengerDetailsByCustomerIdAndPassengerId(@Param("customerId") int customerId, @Param("passengerId") int passengerId);

	@Query("SELECT p FROM Passenger p WHERE p.id = :id")
	Passenger findPassengerById(final int id);

	@Query("""
		    SELECT DISTINCT br.booking.customer
		    FROM BookingRecord br
		    WHERE br.passenger.id = :passengerId
		""")
	Collection<Customers> findCustomerByPassengerId(@Param("passengerId") int passengerId);

	@Query("Select c from Customers c where c.id=:id")
	Customers findCustomerById(final int id);

	@Query("select b.passenger from BookingRecord b  WHERE b.booking.id = :bookingId and b.booking.customer.id =:id")
	Collection<Passenger> findPassengersByBookingId(@Param("bookingId") int bookingId, @Param("id") int id);

	@Query("SELECT b from Booking b WHERE b.id=:id")
	Booking findBookinById(final int id);

}
