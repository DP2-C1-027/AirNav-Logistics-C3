/*
 * TrackingLogRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.entities.claims;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	//Listar todos los TrackingLog asociados a una Claim específica
	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

	// Obtener un TrackingLog específico por su ID
	@Query("SELECT t FROM TrackingLog t WHERE t.id = :trackingLogId")
	TrackingLog findOneTrackingLogById(int trackingLogId);

	// Listar todos los TrackingLog (opcional, si es necesario)
	@Query("SELECT t FROM TrackingLog t")
	Collection<TrackingLog> findAllTrackingLogs();

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId ORDER BY t.lastUpdateMoment DESC")
	List<TrackingLog> findTrackingLogsByClaimIdOrderedByDateDesc(int claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId ORDER BY t.lastUpdateMoment ASC")
	List<TrackingLog> findTrackingLogsByClaimIdOrderedByDateAsc(int claimId);

}
