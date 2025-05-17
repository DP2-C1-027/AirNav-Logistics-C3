
package acme.entities.service;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidDiscount;
import acme.constraints.ValidDwellTime;
import acme.constraints.ValidPromotionCode;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Service extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidShortText
	private String				name;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@Automapped
	@ValidUrl
	private String				picture;

	@Mandatory
	@Automapped
	@ValidDwellTime
	private Double				averageDwellTime;

	@Optional
	@Automapped
	@ValidPromotionCode
	private String				promotionCode;

	@Optional
	@Automapped
	@ValidDiscount
	private Double				money;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
