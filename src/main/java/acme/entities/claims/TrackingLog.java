
package acme.entities.claims;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingLog extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Past
	@Automapped
	private java.util.Date		lastUpdateMoment;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				stepUndergoing;

	@Mandatory
	@NotNull
	@Automapped
	private Double				resolutionPercentage;

	@Mandatory
	@Automapped
	private Boolean				isFinalDecision;

	@Optional
	@ValidLongText
	@Automapped
	private String				resolutionDetails;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne
	@Valid
	private Claim				claim;

}
