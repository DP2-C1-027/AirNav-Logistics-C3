
package acme.features.administrator.systemConfiguration;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.systemConfiguration.SystemConfiguration;

@Repository
public interface SystemConfigurationRepository extends AbstractRepository {

	@Modifying
	@Query("UPDATE SystemConfiguration sc SET sc.systemCurrency = false WHERE sc.systemCurrency = true")
	void deactivateAllCurrencies();

	@Query("select s from SystemConfiguration s where s.systemCurrency=true")
	SystemConfiguration findActive();

	@Query("select s from SystemConfiguration s")
	Collection<SystemConfiguration> getSystem();

	@Query("SELECT sc FROM SystemConfiguration sc WHERE sc.currency = :currency")
	SystemConfiguration findByCurrency(@Param("currency") String currency);

	@Query("select s from SystemConfiguration s where s.id=:id")
	SystemConfiguration findById(int id);

}
