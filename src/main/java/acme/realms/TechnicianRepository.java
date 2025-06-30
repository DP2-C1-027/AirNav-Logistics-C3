
package acme.realms;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TechnicianRepository extends AbstractRepository {

	@Query("SELECT t FROM Technician t WHERE t.codigo = :codigo AND t.id != :id")
	Optional<Technician> findByLicenseNumber(String codigo, int id);

}
