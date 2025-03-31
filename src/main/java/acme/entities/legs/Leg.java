
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
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidFlightNumber;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flights.Flight;
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
	@Column(unique = true)
	@ValidFlightNumber
	private String				flightNumber;

	@Mandatory
	@Automapped
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	private Date				scheduledDeparture;

	@Mandatory
	@Automapped
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	private Date				scheduledArrival;

	@Mandatory
	@Automapped
	@Min(0)
	private Integer				duration;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private LegStatus			status;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne
	@Valid
	@Automapped
	private Airport				departureAirport;

	@Mandatory
	@ManyToOne
	@Valid
	@Automapped
	private Airport				arrivalAirport;

	@Mandatory
	@ManyToOne
	@Valid
	@Automapped
	private Aircraft			aircraft;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	@Automapped
	private Flight				flight;
}
