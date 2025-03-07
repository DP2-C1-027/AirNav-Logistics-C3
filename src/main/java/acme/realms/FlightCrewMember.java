
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.constraints.ValidIdentifier;
import acme.constraints.ValidLongText;
import acme.constraints.ValidPhoneNumber;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightCrewMember extends AbstractRole {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidIdentifier
	@Column(unique = true)
	private String				employeeCode;

	@Mandatory
	@Automapped
	@ValidPhoneNumber
	private String				phoneNumber;

	@Mandatory
	@Automapped
	@ValidLongText
	private String				languageSkills;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private AvailabilityStatus	availabilityStatus;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				salary;

	@Optional
	@Automapped
	@Min(0)
	private Integer				yearsOfExperience;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airline				airline;

}
