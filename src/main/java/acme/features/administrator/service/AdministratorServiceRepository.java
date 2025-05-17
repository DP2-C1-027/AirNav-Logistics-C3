
package acme.features.administrator.service;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.service.Service;

@Repository
public interface AdministratorServiceRepository extends AbstractRepository {

	@Query("SELECT b FROM Service b WHERE b.id=:id")
	Service findService(final int id);

	@Query("SELECT a FROM Service a")
	Collection<Service> findAllService();

	@Query("SELECT a FROM Service a WHERE a.promotionCode=:promotionCode")
	Collection<Service> findByPromotionCode(String promotionCode);
}
