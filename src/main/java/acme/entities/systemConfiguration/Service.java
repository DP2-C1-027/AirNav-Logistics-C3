
package acme.entities.systemConfiguration;

import java.util.List;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Service extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	private String				systemCurrency		= "EUR";

	@Mandatory
	@Automapped
	private List<String>		acceptedCurrencies	= List.of("EUR", "USD", "GBP");

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
