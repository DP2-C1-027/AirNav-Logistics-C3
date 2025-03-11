
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.booking.TravelClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomersDashboards extends AbstractForm {
	// Serialisation version --------------------------------------------------

	private static final long			serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private List<String>				theLastFiveDestinations;
	Double								moneySpentInBookingDuringLastYear;
	private Map<TravelClass, Integer>	bookingsGroupedByTravelClass;

	Integer								countOfTheCostBooking5Years;
	Double								averageOfTheCostBooking5Years;
	Double								minOfTheCostBooking5Years;
	Double								maxOfTheCostBooking5Years;
	Double								desviationOfTheCostBooking5Years;

	Integer								countOfTheNumberOfPassengersBookings;
	Double								averageOfTheNumberOfPassengersBookings;
	Double								minOfTheNumberOfPassengersBookings;
	Double								maxOfTheNumberOfPassengersBookings;
	Double								desviationOfTheNumberOfPassengersBookings;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
