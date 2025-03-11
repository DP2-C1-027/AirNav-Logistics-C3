
package acme.entities.flights;

import java.util.Comparator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidShortText
	private String				tag;

	@Mandatory
	@Automapped
	private Boolean				indication;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				cost;

	@Optional
	@Automapped
	@ValidLongText
	private String				description;

	// Derived attributes -----------------------------------------------------


	@Transient
	private java.util.Date getScheduledDeparture() {

		java.util.Date result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.getLegs(this.getId());
		result = legs.stream().min(Comparator.comparing(Leg::getScheduledArrival)).get().getScheduledArrival();
		return result;
	};

	@Transient
	private java.util.Date getScheduledArrival() {

		java.util.Date result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.getLegs(this.getId());
		result = legs.stream().max(Comparator.comparing(Leg::getScheduledArrival)).get().getScheduledArrival();
		return result;
	};

	@Transient
	private String getDepartureCity() {
		String result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.getLegs(this.getId());
		result = legs.stream().min(Comparator.comparing(Leg::getScheduledArrival)).get().getArrivalAirport().getCity();
		return result;
	};

	@Transient
	private String getArrivalCity() {
		String result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.getLegs(this.getId());
		result = legs.stream().max(Comparator.comparing(Leg::getScheduledArrival)).get().getArrivalAirport().getCity();
		return result;
	};

	@Transient
	private Integer getLayovers() {
		Integer result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		result = repository.getLegs(this.getId()).size();
		return result;
	};

	// Relationships ----------------------------------------------------------


	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private AirlineManager airlineManager;

}
