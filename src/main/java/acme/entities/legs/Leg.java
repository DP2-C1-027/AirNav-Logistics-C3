
package acme.entities.legs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.constraints.ValidIATAcode;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidIATAcode
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@Automapped
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@Automapped
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Automapped
	@Min(0)
	private Integer				duration;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private LegStatus			status;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne
	@Valid
	private Airport				departureAirport;

	@Mandatory
	@ManyToOne
	@Valid
	private Airport				arrivalAirport;

	//	@Mandatory
	//	@OneToOne
	// 	@Valid
	//	private Aircraft			aircraft;
}
