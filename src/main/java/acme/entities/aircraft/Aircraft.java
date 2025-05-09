
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidCapacity;
import acme.constraints.ValidCargoWeight;
import acme.constraints.ValidLongTextOptional;
import acme.constraints.ValidShortText;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "registrationNumber")
})
public class Aircraft extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidShortText
	@Automapped
	private String				model;

	@Mandatory
	@ValidShortText
	@Automapped
	@Column(unique = true)
	private String				registrationNumber;

	@Mandatory
	@Automapped
	@ValidCapacity
	private Integer				capacity;

	@Mandatory
	@Automapped
	@ValidCargoWeight
	private Integer				cargoWeight;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private Status				status;

	@Optional
	@Automapped
	@ValidLongTextOptional
	private String				details;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airline				airline;

}
