
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.scheduledDeparture >= :moment")
	Collection<FlightAssignment> findAllPlannedFlightAssignments(Date moment);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.scheduledArrival < :moment")
	Collection<FlightAssignment> findAllCompletedFlightAssignments(Date moment);

	@Query("SELECT fcm FROM FlightCrewMember fcm WHERE fcm.airline.id = :airlineId")
	Collection<FlightCrewMember> findAllflightCrewMemberFromAirline(int airlineId);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.id = :legId AND fa.duty = :duty")
	FlightAssignment findFlightAssignmentByLegAndDuty(int legId, Duty duty);

	@Query("SELECT COUNT(fa) > 0 FROM FlightAssignment fa WHERE fa.leg.id = :legId AND fa.duty IN ('PILOT', 'CO_PILOT') AND fa.duty = :duty AND fa.id != :id")
	Boolean hasDutyAssigned(int legId, Duty duty, int id);

	@Query("SELECT COUNT(fa) > 0 FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :flightCrewMemberId AND fa.moment = :moment")
	Boolean hasFlightCrewMemberLegAssociated(int flightCrewMemberId, Date moment);

	@Query("SELECT l FROM Leg l WHERE l.aircraft.airline.id = :airlineId")
	Collection<Leg> findAllLegsFromAirline(int airlineId);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();

}
