
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

	// Amount of airports grouped by operational scope
	private Map<OperationalScope, Integer>	amountAirportsGroupedByOperationalScope;

	// Amount of airlines grouped by type
	private Map<AirlineType, Integer>		amountAirlineGroupedByType;

	// Ratio of all airlines with both an email and phone number
	private Double							ratioAirlinesEmailAndPhone;

	// Ratio of active and nonActive aircrafts
	private Double							ratioActiveAircrafts;
	private Double							ratioInactiveAircrafts;

	// Ratio of all reviews with a score above 5.00
	private Double							ratioHighScoreReviews;

	// Count, Average, Minimum, Maximum, and deviation of the number of reviews posted over the last 10 weeks
	private Statistics						adminReviews;
}
