
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidLastNibble;
import acme.constraints.ValidLocatorCode;
import acme.realms.booking.Customers;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidLocatorCode
	@Automapped
	private String				locatorCode;

	@Mandatory
	//valid en el pasado
	@ValidMoment
	@Automapped
	private Date				purchaseMoment;

	@Mandatory
	@Automapped
	private TravelClass			travelClass;

	@Mandatory
	@Automapped
	@Min(0)
	private Double				price;

	@Optional
	@Automapped
	@ValidLastNibble
	private String				lastNibble;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@ManyToOne
	@Mandatory
	@Valid
	private Customers			customer;

	//@ManyToOne
	//@Valid
	//@Mandatory
	//private Flight flight;

}
