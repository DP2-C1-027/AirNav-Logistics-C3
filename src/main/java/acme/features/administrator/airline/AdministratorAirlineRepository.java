
package acme.features.administrator.airline;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;

@Repository
public interface AdministratorAirlineRepository extends AbstractRepository {

	@Query("SELECT b FROM Airline b WHERE b.id=:id")
	Airline findAirline(final int id);

	@Query("SELECT a FROM Airline a")
	Collection<Airline> findAllAirline();

}
