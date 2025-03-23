/*
 * AssistanceAgentTrackingLogRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.assistanceAgent.trackingLogs;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.TrackingLog;;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	//Listar todos los TrackingLog asociados a una Claim específica
	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

	// Obtener un TrackingLog específico por su ID
	@Query("SELECT t FROM TrackingLog t WHERE t.id = :trackingLogId")
	TrackingLog findOneTrackingLogById(int trackingLogId);

	// Listar todos los TrackingLog (opcional, si es necesario)
	@Query("SELECT t FROM TrackingLog t")
	Collection<TrackingLog> findAllTrackingLogs();

}
