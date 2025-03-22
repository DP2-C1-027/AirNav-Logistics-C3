
package acme.features.customers.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.Passenger;

@Repository
public interface CustomersBookingRecordRepository extends AbstractRepository {

	@Query("select b.passenger from BookingRecord b  WHERE b.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBookingId(@Param("bookingId") int bookingId);

	@Query("SELECT b from Booking b WHERE b.id=:id")
	Booking findBookinById(final int id);

}
