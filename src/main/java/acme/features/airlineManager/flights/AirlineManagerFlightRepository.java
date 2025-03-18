
package acme.features.airlineManager.flights;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f")
	Collection<Flight> getAllFlights();

	@Query("SELECT f FROM Flight f WHERE f.id=:id")
	Flight findFlightById(int id);
}
