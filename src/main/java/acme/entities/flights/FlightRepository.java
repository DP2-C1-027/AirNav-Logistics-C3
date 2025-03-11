
package acme.entities.flights;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.components.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.legs.Leg;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight = :flightId")
	List<Leg> getLegs(int flightId);

	@Query("SELECT f.cost FROM Flight f WHERE f.id = :flightId")
	Money findCostByFlightId(@Param("flightId") int flightId);
}
