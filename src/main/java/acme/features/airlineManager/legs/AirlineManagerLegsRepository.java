
package acme.features.airlineManager.legs;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.legs.Leg;

@Repository
public interface AirlineManagerLegsRepository extends AbstractRepository {

	@Query("SELECT l FROM Leg l")
	Collection<Leg> getAllLegs();

	@Query("SELECT l FROM Leg l WHERE l.id=:id")
	Leg findLegById(int id);
}
