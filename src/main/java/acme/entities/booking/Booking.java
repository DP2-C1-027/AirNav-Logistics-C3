
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidLastNibble;
import acme.constraints.ValidLocatorCode;
import acme.entities.flights.Flight;
import acme.realms.Customers;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "locatorCode"), @Index(columnList = "locatorCode, id"), @Index(columnList = "customer_id")
})
public class Booking extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidLocatorCode
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@Automapped
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	@Valid
	private TravelClass			travelClass;

	@Optional
	@Automapped
	@ValidLastNibble
	private String				lastNibble;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Money getPrice() {

		Money result;
		BookingRepository repository;

		repository = SpringHelper.getBean(BookingRepository.class);
		result = repository.findCostByBookingId(this.getId());

		return result;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Customers	customer;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Flight		flight;

}
