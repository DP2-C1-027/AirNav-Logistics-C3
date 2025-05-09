
package acme.entities.systemConfiguration;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "systemCurrency"), @Index(columnList = "currency")
})
public class SystemConfiguration extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	private String				currency;

	@Mandatory
	@Automapped
	private Boolean				systemCurrency;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
