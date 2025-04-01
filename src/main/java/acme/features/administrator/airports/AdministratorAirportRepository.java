
package acme.features.administrator.airports;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airport.Airport;

@Repository
public interface AdministratorAirportRepository extends AbstractRepository {

	@Query("SELECT b FROM Airport b WHERE b.id=:id")
	Airport findAirport(final int id);

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirport();

	@Query("select b from Airport b where b.codigo =:codigo")
	Collection<Airport> findAllAirportCode(@Param("codigo") String codigo);

}
