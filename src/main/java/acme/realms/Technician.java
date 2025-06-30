
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidIdentifier;
import acme.constraints.ValidLongTextOptional;
import acme.constraints.ValidPhoneNumber;
import acme.constraints.ValidShortText;
import acme.constraints.ValidTechnician;
import acme.constraints.ValidYearsOfExperience;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@ValidTechnician
public class Technician extends AbstractRole {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidIdentifier
	@Column(unique = true)
	private String				codigo;

	@Mandatory
	@Automapped
	@ValidPhoneNumber
	private String				phoneNumber;

	@Mandatory
	@Automapped
	@ValidShortText
	private String				specialisation;

	@Mandatory
	@Automapped
	@Valid
	private Boolean				annualHealthTest;

	@Mandatory
	@Automapped
	@ValidYearsOfExperience
	private Integer				yearsOfExperience;

	@Optional
	@Automapped
	@ValidLongTextOptional
	private String				certifications;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
