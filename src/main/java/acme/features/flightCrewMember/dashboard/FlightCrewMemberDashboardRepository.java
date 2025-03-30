/*
 * FlightCrewMemberDashboardRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.flightCrewMember.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightCrewMemberDashboardRepository extends AbstractRepository {

	@Query("SELECT DISTINCT l.arrivalAirport.name FROM FlightAssignment f JOIN f.leg l WHERE f.flightCrewMember.id = :flightCrewMemberId ORDER BY f.moment DESC")
	List<String> findLastFiveDestinations(int flightCrewMemberId, Pageable pageable);

	@Query("SELECT COUNT(DISTINCT a.flightAssignment.leg) FROM ActivityLog a WHERE a.severityLevel BETWEEN :innerRange AND :outerRange")
	Integer countLegsWithSeverity(int innerRange, int outerRange);

	@Query("SELECT DISTINCT f.flightCrewMember.codigo FROM FlightAssignment f WHERE f.leg = (SELECT fa.leg FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :flightCrewMemberId AND fa.moment = (SELECT MAX(fa2.moment) FROM FlightAssignment fa2 WHERE fa2.flightCrewMember.id = :flightCrewMemberId))")
	List<String> findCrewMembersInLastLeg(int flightCrewMemberId);

	@Query("SELECT f.currentStatus, COUNT(f) FROM FlightAssignment f WHERE f.flightCrewMember.id = :flightCrewMemberId GROUP BY f.currentStatus")
	List<Object[]> countFlightAssignmentsGroupedByStatus(int flightCrewMemberId);

	@Query("SELECT COUNT(f) FROM FlightAssignment f WHERE f.moment >= :moment AND f.flightCrewMember.id = :crewMemberId")
	Integer countFlightAssignmentsLastMonth(Date moment, int crewMemberId);

	//	@Query("SELECT AVG(COUNT(f)) FROM FlightAssignment f WHERE f.moment >= :moment AND f.flightCrewMember.id = :crewMemberId")
	//	Double averageFlightAssignmentsLastMonth(Date moment, int crewMemberId);
	//
	//	@Query("SELECT MIN(COUNT(f)) FROM FlightAssignment f WHERE f.moment >= :moment AND f.flightCrewMember.id = :crewMemberId")
	//	Double minFlightAssignmentsLastMonth(Date moment, int crewMemberId);
	//
	//	@Query("SELECT MAX(COUNT(f)) FROM FlightAssignment f WHERE f.moment >= :moment AND f.flightCrewMember.id = :crewMemberId")
	//	Double maxFlightAssignmentsLastMonth(Date moment, int crewMemberId);
	//
	//	@Query(value = "SELECT SQRT(SUM(POW(sub.countFA - :avgValue, 2)) / COUNT(sub.countFA)) FROM (SELECT COUNT(*) AS countFA FROM flight_assignment WHERE moment >= :moment AND flight_crew_member_id = :crewMemberId GROUP BY flight_crew_member_id) sub",nativeQuery = true)
	//	Double deviationFlightAssignmentsLastMonth(Date moment, int crewMemberId, Double avgValue);

}
