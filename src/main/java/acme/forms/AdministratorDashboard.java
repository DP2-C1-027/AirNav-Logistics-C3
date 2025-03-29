
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.Statistics;
import acme.entities.airline.AirlineType;
import acme.entities.airport.OperationalScope;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long				serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// amount of airports grouped by operational scope

	private Map<OperationalScope, Integer>	amountAirportsGroupedByOperationalScope;

	// amount of airlines grouped by type

	private Map<AirlineType, Integer>		amountAirlineGroupedByType;

	// ratio of all airlines with both an email and phone number
	private Double							ratioAirlinesEmailAndPhone;

	// ratio of active and nonActive aircrafts
	private Double							ratioActiveAircrafts;

	// ratio of all reviews with a score above 5.00
	private Double							ratioHighScoreReviews;

	// count, average, minimum, maximum, and deviation of the number of reviews posted over the last 10 weeks
	private Statistics						adminReviews;
}
