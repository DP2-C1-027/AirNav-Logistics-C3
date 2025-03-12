
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.LegStatistics;
import acme.entities.airport.Airport;
import acme.entities.legs.LegStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirlineManagerDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Ranking based on years of service, amongst all managers
	private Integer					ranking;

	// years to retire assuming retirement at 65 years of age
	private Integer					yearsToRetire;

	// ratio between legs with OnTime status and legs with Delayed status
	private Double					ratioOnTimeAndDelayedLegs;

	// airport with the most flights
	private Airport					mostPopularAirport;

	// airport with the least flights 
	private Airport					leastPopularAirport;

	// amount of legs grouped by status
	private Map<LegStatus, Integer>	legsCountByStatus;

	//minimum,maximum,average and deviation of flight prices
	private LegStatistics			flightCost;
}
