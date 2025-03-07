
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidLongText;
import acme.constraints.ValidPassportNumber;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BannedPassenger extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidLongText
	@Automapped
	private String				fullName;

	@Mandatory
	@Automapped
	@ValidPassportNumber
	private String				passportNumber;

	@Mandatory
	@Automapped
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateOfBirth;

	@Mandatory
	@Automapped
	@ValidShortText
	private String				nationality;

	@Mandatory
	@Automapped
	@ValidLongText
	private String				reason;

	@Mandatory
	@Automapped
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateOfBan;

	@Optional
	@Automapped
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateOfLift;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
