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
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.claims.TrackingLog;
import acme.entities.legs.Leg;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("SELECT c FROM Claim c WHERE NOT EXISTS (SELECT t FROM TrackingLog t WHERE t.claim = c AND t.indicator IN (acme.entities.claims.Indicator.ACCEPTED, acme.entities.claims.Indicator.REJECTED))")
	Collection<Claim> findAllUndergoingClaims();

	@Query("SELECT c FROM Claim c WHERE EXISTS (SELECT t FROM TrackingLog t WHERE t.claim = c AND t.indicator IN (acme.entities.claims.Indicator.ACCEPTED, acme.entities.claims.Indicator.REJECTED))")
	Collection<Claim> findAllCompletedClaims();

	@Query("SELECT c FROM Claim c WHERE c.registeredBy.id = :assistanceAgentId AND NOT EXISTS (SELECT t FROM TrackingLog t WHERE t.claim = c AND t.indicator IN (acme.entities.claims.Indicator.ACCEPTED, acme.entities.claims.Indicator.REJECTED))")
	Collection<Claim> findAllUndergoingClaimsByAgent(int assistanceAgentId);

	@Query("SELECT c FROM Claim c WHERE c.registeredBy.id = :assistanceAgentId AND EXISTS (SELECT t FROM TrackingLog t WHERE t.claim = c AND t.indicator IN (acme.entities.claims.Indicator.ACCEPTED, acme.entities.claims.Indicator.REJECTED))")
	Collection<Claim> findAllCompletedClaimsByAgent(int assistanceAgentId);

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findOneClaimById(int id);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

	@Query("SELECT t.claim FROM TrackingLog t WHERE t.id = :trackingLogId")
	Claim findClaimByTrackingLogId(int trackingLogId);

	@Query("SELECT c FROM Claim c WHERE c.linkedTo.id=:legId")
	Collection<Claim> findClaimsByLegId(int legId);

	@Query("SELECT l FROM Claim c JOIN Leg l ON c.linkedTo = l.id WHERE c.id = :claimId")
	Leg findLinkedLegByClaimId(int claimId);

	@Query("SELECT l FROM Leg l WHERE l.draftMode = false AND l.scheduledArrival < (SELECT c.registrationMoment FROM Claim c WHERE c.id = :claimId)")
	Collection<Leg> findCompletedLegsByClaimId(int claimId);

	@Query("SELECT l FROM Leg l WHERE l.draftMode = false AND l.scheduledArrival < ( :registrationMoment)")
	Collection<Leg> findCompletedLegsByRegistrationMoment(Date registrationMoment);

	@Query("SELECT c FROM Claim c WHERE c.linkedTo.flight.id = :flightId")
	Collection<Claim> findClaimsByFlightId(int flightId);
}
