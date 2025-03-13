
package acme.entities.visaRequirements;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class VisaRequirements extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidShortText
	private String				country;

	@Mandatory
	@Automapped
	@ValidShortText
	private String				nationality;

	@Mandatory
	@Automapped
	private Boolean				visaRequired;

	@Mandatory
	@Automapped
	@ValidShortText
	private String				visaType;

	@Mandatory
	@Automapped
	@ValidLongText
	private String				additionalInfo;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
