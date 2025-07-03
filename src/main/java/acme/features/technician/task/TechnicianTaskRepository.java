
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Passenger;
import acme.entities.maintanenceRecords.InvolvedIn;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.Task;
import acme.realms.Technician;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	//listar todos los passenger de un customer 

	//cambiado!
	@Query("select p from Passenger p where p.customer.id=:customerId")
	Collection<Passenger> findPassengersByCustomerId(@Param("customerId") int customerId);

	@Query("select p from Task p where p.technician.id=:technicianId")
	Collection<Task> findTasksByTechnicianId(@Param("technicianId") int technicianId);

	@Query("select p from Task p where p.draftMode=0")
	Collection<Task> findPublishedTasks();

	@Query("select br from InvolvedIn br where br.task.id=:id")
	Collection<InvolvedIn> findAllInvolvedInById(int id);

	@Query("SELECT p FROM Passenger p WHERE p.id = :id")
	Passenger findPassengerById(final int id);

	@Query("Select c from Technician c where c.id=:id")
	Technician findTechnicianById(final int id);

	@Query("select b.task from InvolvedIn b  WHERE b.maintanenceRecord.id = :recordId")
	Collection<Task> findTasksByTechId(@Param("recordId") int recordId);

	@Query("SELECT b from Task b WHERE b.id=:id")
	Task findTaskById(final int id);

	@Query("SELECT b from MaintanenceRecord b WHERE b.id=:id")
	MaintanenceRecord findRecordById(final int id);

}
