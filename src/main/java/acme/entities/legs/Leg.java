
package acme.entities.legs;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.constraints.ValidShortText;
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
	@ValidShortText
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date		scheduledDeparture;

	@Mandatory
	@Automapped
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date		scheduledArrival;

	@Mandatory
	@Automapped
	private Airport				departureAirport;

	@Mandatory
	@Automapped
	private Airport				arrivalAirport;

	@Mandatory
	@Automapped
	private Integer				duration;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private LegStatus			status;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
