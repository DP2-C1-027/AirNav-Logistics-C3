
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select a from ActivityLog a where a.id = :id")
	ActivityLog findActivityLogRecordById(int id);

	@Query("select a from ActivityLog a")
	Collection<ActivityLog> findAllActivityLogRecords();

	@Query("select a from ActivityLog a where a.flightAssignment.id = :flightAssignmentId")
	Collection<ActivityLog> findActivityLogsByFlightAssignmentId(int flightAssignmentId);

}
