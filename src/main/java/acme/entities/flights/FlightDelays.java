
package acme.entities.flights;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

// parte del D02 S2 student4
@Entity
@Getter
@Setter
public class FlightDelays extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidShortText
	private String				flightName;

	@Mandatory
	@Automapped
	@ValidShortText
	private String				status;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
