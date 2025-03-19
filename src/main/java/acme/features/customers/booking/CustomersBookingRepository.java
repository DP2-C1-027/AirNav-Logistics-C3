
package acme.features.customers.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.Passenger;

@Repository
public interface CustomersBookingRepository extends AbstractRepository {

	//listar los booking de un customer

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findByCustomer(@Param("customerId") final int customerId);

	//show details and passengers

	Booking findBookinById(final int id);

	@Query("select b.passenger from BookingRecord b  WHERE b.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBookingId(@Param("bookingId") int bookingId);
	//create and update

}
