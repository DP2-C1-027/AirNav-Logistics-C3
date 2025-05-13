
package acme.entities.claims;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidLongText;
import acme.entities.legs.Leg;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@Automapped
	@ValidEmail
	private String				passengerEmail;

	@Mandatory
	@Automapped
	@ValidLongText
	private String				description;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private ClaimType			type;

	@Mandatory
	@Automapped
	private boolean				draftMode;


	// Derived attributes -----------------------------------------------------
	@Transient
	public Indicator getIndicator() {
		TrackingLogRepository trackingLogRepository;

		trackingLogRepository = SpringHelper.getBean(TrackingLogRepository.class);

		List<TrackingLog> logs = trackingLogRepository.findTrackingLogsByClaimIdOrderedByDateDesc(this.getId());

		if (logs == null || logs.isEmpty())
			return Indicator.PENDING;

		return logs.stream().filter(tl -> tl.getIndicator() == Indicator.ACCEPTED || tl.getIndicator() == Indicator.REJECTED).findFirst().map(TrackingLog::getIndicator).orElse(Indicator.PENDING);
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@ManyToOne
	@Valid
	private AssistanceAgent	registeredBy;

	@Mandatory
	@ManyToOne
	@Valid
	private Leg				linkedTo;

}
