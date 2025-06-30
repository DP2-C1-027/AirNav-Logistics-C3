
package acme.entities.maintanenceRecords;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.constraints.ValidLongText;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "draftMode"),//
	@Index(columnList = "draftMode, technician_id")
})
public class Task extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private TaskType			type;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@Automapped
	@ValidLongText
	private String				description;

	@Mandatory
	@Automapped
	@Min(0)
	@Max(10)
	private Integer				priority;

	@Mandatory
	@Automapped
	@Min(1)
	@Max(1000)
	private Integer				estimatedDuration;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
	@ManyToOne(optional = false)
	@Mandatory
	@Valid
	private Technician			technician;
}
