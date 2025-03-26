
package acme.features.customers.bookingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.Passenger;

@Repository
public interface CustomersBookingRecordRepository extends AbstractRepository {

	@Query("select b from BookingRecord b where b.booking.customer.id=:customerId")
	Collection<BookingRecord> findAllBRecordByCustomerId(int customerId);

	@Query("select b from Booking b where b.id=:id")
	Booking findBookingById(int id);

	@Query("select b.booking from BookingRecord b where b.id=?1")
	Booking findOneBookingByBookingRecord(int id);

	@Query("select b.passenger from BookingRecord b where b.id=?1")
	Passenger findOnePassengerByBookingRecord(int id);

	@Query("select b from BookingRecord b where b.id=:id")
	BookingRecord findBookingRecord(int id);

	@Query("select b from Booking b where b.customer.id=:id")
	Collection<Booking> findBookingByCustomerId(int id);

	@Query("select b from Passenger b where b.customer.id=:id")
	Collection<Passenger> findPassengerByCustomerId(int id);

	@Query("SELECT COUNT(b) > 0 FROM BookingRecord b WHERE b.booking = :booking AND b.passenger = :passenger")
	boolean existsByBookingAndPassenger(@Param("booking") Booking booking, @Param("passenger") Passenger passenger);

	@Query("select b from Booking b where b.customer.id=:id and b.draftMode=:draftMode")
	Collection<Booking> findNotPublishBooking(@Param("id") int id, @Param("draftMode") boolean draftMode);
}
