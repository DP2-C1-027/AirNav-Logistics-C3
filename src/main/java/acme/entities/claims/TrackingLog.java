
package acme.entities.claims;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidLongTextOptional;
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
	@Automapped
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdateMoment;

	@Mandatory
	@Automapped
	@ValidShortText
	private String				stepUndergoing;

	@Mandatory
	@Automapped
	@Min(0)
	private Double				resolutionPercentage;

	@Mandatory
	@Automapped
	private Boolean				indicator;

	@Optional
	@Automapped
	@ValidLongTextOptional
	private String				resolutionDetails;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne
	@Valid
	private Claim				claim;

}
