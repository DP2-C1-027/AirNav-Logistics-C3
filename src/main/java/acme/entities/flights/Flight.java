
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
import acme.constraints.ValidLongTextOptional;
import acme.constraints.ValidShortText;
import acme.entities.airline.Airline;
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
	@ValidMoney(max = 1000000, min = 0)
	private Money				cost;

	@Optional
	@Automapped
	@ValidLongTextOptional
	private String				description;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public java.util.Date getScheduledDeparture() {

		java.util.Date result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.getLegs(this);
		Leg l = legs.stream().min(Comparator.comparing(Leg::getScheduledDeparture)).orElse(null);
		result = l == null ? null : l.getScheduledDeparture();
		return result;
	};

	@Transient
	public java.util.Date getScheduledArrival() {

		java.util.Date result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.getLegs(this);
		Leg l = legs.stream().max(Comparator.comparing(Leg::getScheduledArrival)).orElse(null);
		result = l == null ? null : l.getScheduledArrival();
		return result;
	};

	@Transient
	public String getDepartureCity() {
		String result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.getLegs(this);
		Leg l = legs.stream().min(Comparator.comparing(Leg::getScheduledArrival)).orElse(null);
		result = l == null ? null : l.getDepartureAirport().getCity();
		return result;
	};

	@Transient
	public String getArrivalCity() {
		String result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.getLegs(this);
		Leg l = legs.stream().max(Comparator.comparing(Leg::getScheduledArrival)).orElse(null);
		result = l == null ? null : l.getArrivalAirport().getCity();
		return result;
	};

	@Transient
	public Integer getLayovers() {
		Integer result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		result = repository.getLegs(this).size() - 1;
		result = result >= 0 ? result : null;
		return result;
	};

	// Relationships ----------------------------------------------------------


	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private AirlineManager	airlineManager;

	@ManyToOne(optional = false)
	@Valid
	private Airline			airline;

}
