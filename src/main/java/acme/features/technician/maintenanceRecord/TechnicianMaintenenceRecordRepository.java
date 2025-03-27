
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintanenceRecords.MaintanenceRecord;

@Repository
public interface TechnicianMaintenenceRecordRepository extends AbstractRepository {

	@Query("SELECT f FROM MaintanenceRecord f")
	Collection<MaintanenceRecord> getAllMaintanenceRecords();

	@Query("SELECT f FROM MaintanenceRecord f WHERE f.technician.id = :technicianId")
	Collection<MaintanenceRecord> getMyMaintanenceRecords(int technicianId);

	@Query("SELECT f FROM MaintanenceRecord f WHERE f.id=:id")
	MaintanenceRecord findRecordById(int id);
}
