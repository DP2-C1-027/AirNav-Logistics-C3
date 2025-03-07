
package acme.entities.maintanenceRecords;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidLongText;
import acme.entities.aircraft.Aircraft;
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
	//valid en el pasado
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	private Date					maintanenceMoment;

	@Mandatory
	@Automapped
	@Valid
	private StatusMaintanenceRecord	status;

	@Mandatory
	@Automapped
	//validMoment pero en el futuro
	@Valid
	private Date					nextMaintanence;

	@Mandatory
	@Automapped
	private Double					estimatedCost;

	@Optional
	@ValidLongText
	@Automapped
	private String					notes;
	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	//muchas instancias de Maintanence estan asociadas a un Aircraft
	@ManyToOne(optional = false)
	@Mandatory
	@Valid
	private Aircraft				aircraft;

}
