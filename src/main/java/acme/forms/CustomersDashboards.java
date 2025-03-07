
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

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	List<String>				theLastFiveDestinations;
	Double						moneySpentInBookingDuringLastYear;
	Map<TravelClass, Integer>	bookingsGroupedByTravelClass;

	Double						countOfTheCostBooking5Years;
	Double						averageOfTheCostBooking5Years;
	Double						minOfTheCostBooking5Years;
	Double						maxOfTheCostBooking5Years;
	Double						desviationOfTheCostBooking5Years;

	Double						countOfTheNumberOfPassengersBookings;
	Double						averageOfTheNumberOfPassengersBookings;
	Double						minOfTheNumberOfPassengersBookings;
	Double						maxOfTheNumberOfPassengersBookings;
	Double						desviationOfTheNumberOfPassengersBookings;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
