
package acme.features.technician.dashboard;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.StatusMaintanenceRecord;
import acme.realms.Technician;

@Repository
public interface TechnicianDashboardRepository extends AbstractRepository {

	@Query("SELECT t FROM Technician t WHERE t.userAccount.id = :id")
	Technician findOneTechnicianByUserAccoundId(int id);

	@Query("SELECT COUNT(m) FROM MaintanenceRecord m WHERE m.technician.id = :technicianId AND m.draftMode = false")
	Integer countMaintanenceRecordsByTechnicianId(int technicianId);

	@Query("""
		    SELECT COUNT(m)
		    FROM MaintanenceRecord m
		    WHERE m.technician.id = :technicianId
		    AND m.status = :status
		    AND m.draftMode = false
		""")
	Optional<Integer> countMaintanenceRecordsByStatus(int technicianId, StatusMaintanenceRecord status);

	@Query("""
		    SELECT m.aircraft
		    FROM InvolvedIn mrt
		    JOIN mrt.maintanenceRecord m
		    WHERE m.technician.id = :technicianId
		    AND m.draftMode = false
		    GROUP BY m.aircraft
		    ORDER BY COUNT(mrt.task) DESC
		""")
	List<Aircraft> findTopFiveAircraftsByTechnicianId(int technicianId);

	@Query("SELECT m FROM MaintanenceRecord m WHERE m.technician.id = :technicianId AND m.draftMode = false ORDER BY m.nextMaintanence ASC")
	List<MaintanenceRecord> findNearestInspectionRecordsByTechnicianId(int technicianId);

	@Query("SELECT AVG(m.estimatedCost.amount) FROM MaintanenceRecord m WHERE m.technician.id = :technicianId AND m.draftMode = false")
	Double findAverageEstimatedCost(int technicianId);

	@Query("SELECT STDDEV(m.estimatedCost.amount) FROM MaintanenceRecord m WHERE m.technician.id = :technicianId AND m.draftMode = false")
	Double findDeviationEstimatedCost(int technicianId);

	@Query("SELECT MIN(m.estimatedCost.amount) FROM MaintanenceRecord m WHERE m.technician.id = :technicianId AND m.draftMode = false")
	Double findMinEstimatedCost(int technicianId);

	@Query("SELECT MAX(m.estimatedCost.amount) FROM MaintanenceRecord m WHERE m.technician.id = :technicianId AND m.draftMode = false")
	Double findMaxEstimatedCost(int technicianId);

	@Query("SELECT AVG(t.estimatedDuration) FROM Task t WHERE t.technician.id = :technicianId AND t.draftMode = false")
	Double findAverageEstimatedDuration(int technicianId);

	@Query("SELECT STDDEV(t.estimatedDuration) FROM Task t WHERE t.technician.id = :technicianId AND t.draftMode = false")
	Double findDeviationEstimatedDuration(int technicianId);

	@Query("SELECT MIN(t.estimatedDuration) FROM Task t WHERE t.technician.id = :technicianId AND t.draftMode = false")
	Double findMinEstimatedDuration(int technicianId);

	@Query("SELECT MAX(t.estimatedDuration) FROM Task t WHERE t.technician.id = :technicianId AND t.draftMode = false")
	Double findMaxEstimatedDuration(int technicianId);
}
