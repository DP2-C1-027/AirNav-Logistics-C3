
package acme.entities.flights;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidShortText
	@Automapped
	private String				tag;

	@Mandatory
	@ValidLongText
	@Automapped
	private String				indication;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@ValidLongText
	@Automapped
	private String				description;

	@Mandatory
	@Automapped
	private java.util.Date		scheduledDeparture;

	@Mandatory
	@Automapped
	private java.util.Date		scheduledArrival;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				departureCity;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				arrivalCity;

	@Mandatory
	@Automapped
	private Integer				layovers;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
