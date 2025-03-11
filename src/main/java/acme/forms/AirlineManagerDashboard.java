
package acme.forms;

import acme.client.components.basis.AbstractForm;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirlineManagerDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Ranking based on years of service, amongst all managers
	private Integer				ranking;

	// years to retire assuming retirement at 65 years of age
	private Integer				yearsToRetire;

	// ratio between legs with OnTime status and legs with Delayed status
	private Double				ratioOnTimeAndDelayedLegs;

	// airport with the most flights
	private Airport				mostPopularAirport;

	// airport with the least flights 
	private Airport				leastPopularAirport;

	// amount of legs grouped by status
	private Integer				onTimeLegs;
	private Integer				delayedLegs;
	private Integer				cancelledLegs;
	private Integer				landedLegs;

	//minimum,maximum,average and deviation of flight prices
	private Integer				minFlightCost;
	private Integer				maxFlightCost;
	private Double				averageFlightCost;
	private Double				deviationFlightCost;
}
