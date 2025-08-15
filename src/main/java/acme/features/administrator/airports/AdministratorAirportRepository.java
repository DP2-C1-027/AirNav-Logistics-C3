
package acme.features.administrator.airports;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airport.Airport;

@Repository
public interface AdministratorAirportRepository extends AbstractRepository {

	@Query("SELECT b FROM Airport b WHERE b.id=:id")
	Airport findAirport(final int id);

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirport();

	@Query("SELECT a FROM Airport a where a.codigo = :code")
	Airport findAirportCode(String code);

}
