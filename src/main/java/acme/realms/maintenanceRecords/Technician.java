
package acme.realms.maintenanceRecords;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Max;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidLongTextOptional;
import acme.constraints.ValidPhoneNumber;
import acme.constraints.ValidShortText;
import acme.constraints.ValidTechnicianLicenseNumber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Technician extends AbstractRealm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidTechnicianLicenseNumber
	@Column(unique = true)
	@Automapped
	private String				licenseNumber;

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
	private Boolean				annualHealthTest;

	@Mandatory
	@Automapped
	@Max(120)
	private Integer				yearsOfExperience;

	@Optional
	@Automapped
	@ValidLongTextOptional
	private String				certifications;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
