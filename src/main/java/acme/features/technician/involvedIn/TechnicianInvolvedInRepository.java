
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.maintanenceRecords.InvolvedIn;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.Task;

@Repository
public interface TechnicianInvolvedInRepository extends AbstractRepository {

	//encontrar todas las task de 
	//cambiado
	@Query("select b from InvolvedIn b where b.maintanenceRecord.technician.id=:technicianId")
	Collection<InvolvedIn> findAllInvolvedInByTechnicianId(int technicianId);

	//ESTE NO SE USA¿¿?¿?¿?¿
	@Query("select b from Booking b where b.id=:id")
	Booking findBookingById(int id);

	//cambiado
	@Query("select b.maintanenceRecord from InvolvedIn b where b.id=?1")
	MaintanenceRecord findOneRecordByInvolvedIn(int id);

	//cambiado
	@Query("select b.task from InvolvedIn b where b.id=?1")
	Task findOneTaskByInvolvedIn(int id);

	//cambiado
	@Query("select b from InvolvedIn b where b.id=:id")
	InvolvedIn findInvolvedIn(int id);

	//encontrar las tasks de un tecnico
	@Query("select b from Task b where b.technician.id=:id")
	Collection<Task> findTaskByTechnicianId(int id);

	//encontrar el record con el tecnico
	@Query("select b from MaintanenceRecord b where b.technician.id=:id")
	Collection<MaintanenceRecord> findRecordByTechnicianId(int id);

	//CAMBIADO
	@Query("SELECT COUNT(b) > 0 FROM InvolvedIn b WHERE b.maintanenceRecord = :maintanenceRecord AND b.task = :task")
	boolean existsByRecordAndTask(@Param("maintanenceRecord") MaintanenceRecord maintanenceRecord, @Param("task") Task task);

	//CAMBIADO
	@Query("select b from MaintanenceRecord b where b.technician.id=:id and b.draftMode=:draftMode")
	Collection<MaintanenceRecord> findNotPublishRecord(@Param("id") int id, @Param("draftMode") boolean draftMode);

}
