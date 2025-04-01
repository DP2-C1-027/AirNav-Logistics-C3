
package acme.entities.systemConfiguration;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SystemConfiguration extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	private String				currency			= "EUR";

	@Mandatory
	@Automapped
	private Boolean				systemCurrency;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
