/*
 * AssistanceAgentClaimRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.assistanceAgent.claims;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.claims.TrackingLog;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("SELECT c FROM Claim c WHERE EXISTS (SELECT t FROM TrackingLog t WHERE t.claim = c AND t.resolutionPercentage BETWEEN 0 AND 99)")
	Collection<Claim> findAllUndergoingClaims();

	@Query("SELECT c FROM Claim c WHERE NOT EXISTS (SELECT t FROM TrackingLog t WHERE t.claim = c AND t.resolutionPercentage < 100)")
	Collection<Claim> findAllCompletedClaims();

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findOneClaimById(int id);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);
}
