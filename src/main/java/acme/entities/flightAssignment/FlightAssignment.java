
package acme.entities.flightAssignment;

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
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidFlightAssignment;
import acme.constraints.ValidLongTextOptional;
import acme.entities.legs.Leg;
import acme.realms.flightcrewmember.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlightAssignment
@Table(name = "flight_assignment", indexes = {
	@Index(name = "idx_fa_crew_draft", columnList = "flight_crew_member_id, draftMode"), @Index(name = "idx_fa_leg_duty_draft", columnList = "leg_id, duty, draftMode")
})
public class FlightAssignment extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private Duty				duty;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private CurrentStatus		currentStatus;

	@Optional
	@Automapped
	@ValidLongTextOptional
	private String				remarks;

	@Mandatory
	@Automapped
	private Boolean				draftMode;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightCrewMember	flightCrewMember;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;

}
