/*
 * AuthenticatedFlightCrewMemberRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.flightCrewMember;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;
import acme.realms.flightcrewmember.FlightCrewMember;

@Repository
public interface AuthenticatedFlightCrewMemberRepository extends AbstractRepository {

	@Query("select a FROM Airline a")
	Collection<Airline> finAllAirlines();

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select f from FlightCrewMember f where f.userAccount.id = :id")
	FlightCrewMember findFlightCrewMemberByUserAccountId(int id);

	@Query("select f from FlightCrewMember f where f.codigo =?1")
	Collection<FlightCrewMember> findFlightCrewMemberCode(String locatorCode);

}
