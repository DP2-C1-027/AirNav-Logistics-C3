
package acme.features.airlineManager.flights;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f")
	Collection<Flight> getAllFlights();

	@Query("SELECT f FROM Flight f WHERE f.id=:id")
	Flight findFlightById(int id);

	@Query("SELECT f FROM Flight f WHERE f.airlineManager=:manager")
	Collection<Flight> getAllFlightsByAirlineManager(AirlineManager manager);

	@Query("SELECT l FROM Leg l WHERE l.flight.id=:id")
	Collection<Leg> findLegsByFlightId(int id);

	@Query("SELECT a FROM Airline a")
	Collection<Airline> getAllAirlines();

	@Query("SELECT a FROM Airline a WHERE a.id = :airlineId")
	Airline getAirlineById(int airlineId);

}
