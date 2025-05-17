
package acme.entities.maintanenceRecords;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Future;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.constraints.ValidLongText;
import acme.entities.aircraft.Aircraft;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintanenceRecord extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean					draftMode;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	@Automapped
	private Date					maintanenceMoment;

	@Mandatory
	@Automapped
	@Valid
	private StatusMaintanenceRecord	status;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	@Future
	@Valid
	private Date					nextMaintanence;

	//este...?
	@Mandatory
	@Automapped
	@ValidMoney
	private Money					estimatedCost;

	@Optional
	@ValidLongText
	@Automapped
	private String					notes;
	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@ManyToOne(optional = false)
	@Mandatory
	@Valid
	private Technician				technician;

	@ManyToOne(optional = false)
	@Mandatory
	@Valid
	private Aircraft				aircraft;

}
