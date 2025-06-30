
package acme.entities.maintanenceRecords;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.constraints.ValidLongTextOptional;
import acme.constraints.ValidNextInspection;
import acme.entities.aircraft.Aircraft;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "draftMode"),// 
	@Index(columnList = "status, technician_id"),//
	@Index(columnList = "technician_id,maintanenceMoment"),//
	@Index(columnList = "technician_id, nextMaintanence")
})
public class MaintanenceRecord extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	private boolean					draftMode;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date					maintanenceMoment;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private StatusMaintanenceRecord	status;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidNextInspection
	private Date					nextMaintanence;

	@Mandatory
	@Automapped
	@ValidMoney(min = 0)
	private Money					estimatedCost;

	@Optional
	@ValidLongTextOptional
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
