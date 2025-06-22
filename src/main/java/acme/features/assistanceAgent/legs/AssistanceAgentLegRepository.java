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

package acme.features.assistanceAgent.legs;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;

@Repository
public interface AssistanceAgentLegRepository extends AbstractRepository {

	@Query("SELECT l FROM Claim c JOIN Leg l ON c.linkedTo = l.id WHERE c.id = :claimId")
	Leg findLinkedLegByClaimId(int claimId);

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findOneClaimById(int id);

}
