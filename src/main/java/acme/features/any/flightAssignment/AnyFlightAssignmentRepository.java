
package acme.features.any.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface AnyFlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :flightAssignmentId")
	FlightAssignment findFlightAssignmentById(int flightAssignmentId);

	// List published assignments
	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.draftMode = false AND fa.leg.draftMode = false")
	Collection<FlightAssignment> findAllPublishedFlightAssignments();

}
