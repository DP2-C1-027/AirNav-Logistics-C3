
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.airport.Airport;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.AvailabilityStatus;
import acme.realms.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	//	Serialisation identifier -----------------------------------------------

	protected static final long								serialVersionUID	= 1L;

	//	Attributes -------------------------------------------------------------

	//	The last five destinations to which they have been assigned. 
	private List<Airport>									lastFiveDestionations;

	//	The number of legs that have an activity log record with an incident severity rang-ing from 0 up to 3, 4 up to 7, and 8 up to 10
	private Map<Integer, Integer>							incidentCountsBySeverity;

	//	The crew members who were assigned with him or her in their last leg.  
	private List<FlightCrewMember>							restOfCrewMembers;

	//	Their flight assignments grouped by their statuses.
	private Map<AvailabilityStatus, List<FlightAssignment>>	flightAssignmentsByStatus;

	//	Minimum, maximum, average and deviation of flight assignments in the last month
	private Integer											minFlightAssignmentCost;
	private Integer											maxFlightAssignmentCost;
	private Double											averageFlightAssignmentCost;
	private Double											deviationFlightAssignmentCost;
}
