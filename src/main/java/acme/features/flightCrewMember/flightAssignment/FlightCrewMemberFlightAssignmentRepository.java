
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.status IN ('ON_TIME', 'DELAYED')")
	Collection<FlightAssignment> findAllPlannedFlightAssignments();

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.status IN ('LANDED', 'CANCELLED')")
	Collection<FlightAssignment> findAllCompletedFlightAssignments();

}
