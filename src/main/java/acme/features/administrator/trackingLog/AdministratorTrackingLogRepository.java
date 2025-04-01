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

package acme.features.administrator.trackingLog;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.claims.TrackingLog;;

@Repository
public interface AdministratorTrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t LEFT JOIN FETCH t.claim WHERE t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

	@Query("SELECT t FROM TrackingLog t LEFT JOIN FETCH t.claim WHERE t.id = :trackingLogId")
	TrackingLog findOneTrackingLogById(int trackingLogId);

	@Query("SELECT t FROM TrackingLog t LEFT JOIN FETCH t.claim")
	Collection<TrackingLog> findAllTrackingLogs();

	@Query("SELECT t FROM TrackingLog t LEFT JOIN FETCH t.claim c WHERE c.registeredBy.id = :AdministratorId")
	Collection<TrackingLog> findAllTrackingLogsByAgent(int AdministratorId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId ORDER BY t.lastUpdateMoment DESC")
	List<TrackingLog> findTrackingLogsByClaimIdOrderedByDateDesc(int claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId ORDER BY t.lastUpdateMoment ASC")
	List<TrackingLog> findTrackingLogsByClaimIdOrderedByDateAsc(int claimId);

	@Query("SELECT t.claim FROM TrackingLog t WHERE t.id = :trackingLogId")
	Claim findClaimByTrackingLogId(int trackingLogId);
}
