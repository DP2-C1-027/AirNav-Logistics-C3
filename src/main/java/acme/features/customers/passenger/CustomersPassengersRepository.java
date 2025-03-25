
package acme.features.customers.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.Passenger;
import acme.realms.Customers;

@Repository
public interface CustomersPassengersRepository extends AbstractRepository {

	//listar todos los passenger de un customer 

	@Query("select p from Passenger p where p.customer.id=:customerId")
	Collection<Passenger> findPassengersByCustomerId(@Param("customerId") int customerId);

	// detalles de un pasajero:

	@Query("SELECT p FROM Passenger p WHERE p.id = :id")
	Passenger findPassengerById(final int id);

	@Query("Select c from Customers c where c.id=:id")
	Customers findCustomerById(final int id);

	@Query("select b.passenger from BookingRecord b  WHERE b.booking.id = :bookingId and b.booking.customer.id =:id")
	Collection<Passenger> findPassengersByBookingId(@Param("bookingId") int bookingId, @Param("id") int id);

	@Query("SELECT b from Booking b WHERE b.id=:id")
	Booking findBookinById(final int id);

}
