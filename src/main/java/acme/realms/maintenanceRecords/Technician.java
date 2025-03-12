
package acme.realms.maintenanceRecords;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.Max;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidIdentifier;
import acme.constraints.ValidLongTextOptional;
import acme.constraints.ValidPhoneNumber;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@ValidIdentifier
public class Technician extends AbstractRole {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@Column(unique = true)
	private String				code;

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
	@Max(120)
	@Valid
	private Integer				yearsOfExperience;

	@Optional
	@Automapped
	@ValidLongTextOptional
	private String				certifications;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
