
package acme.features.administrator.maintanenceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintanenceRecords.MaintanenceRecord;

@Repository
public interface AdministratorRecordRepository extends AbstractRepository {

	@Query("select b from MaintanenceRecord b where b.draftMode=false")
	Collection<MaintanenceRecord> findAllPublishedRecord();

	@Query("select b from MaintanenceRecord b where b.draftMode=false and b.id=:id")
	MaintanenceRecord findById(int id);
}
