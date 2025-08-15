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

package acme.features.administrator.dashboard;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.AirlineType;
import acme.entities.airport.OperationalScope;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository {

	@Query("SELECT COUNT(a) FROM Airport a WHERE a.operationalScope = :op")
	int countAirportsByOperationalScope(OperationalScope op);

	@Query("SELECT COUNT(al) FROM Airline al WHERE al.type = :type")
	int countAirlinesByType(AirlineType type);

	@Query("SELECT (COUNT(al) * 1.0) / (SELECT COUNT(a) FROM Airline a) FROM Airline al WHERE al.email IS NOT NULL AND al.email <> '' AND al.phoneNumber IS NOT NULL AND al.phoneNumber <> ''")
	Double getRatioAirlinesWithEmailAndPhone();

	@Query("SELECT (COUNT(ac) * 1.0) / (SELECT COUNT(a) FROM Aircraft a) FROM Aircraft ac WHERE ac.status = 'ACTIVE_SERVICE'")
	Double getRatioActiveAircrafts();

	@Query("SELECT (COUNT(ac) * 1.0) / (SELECT COUNT(a) FROM Aircraft a) FROM Aircraft ac WHERE ac.status = 'UNDER_MAINTENANCES'")
	Double getRatioInactiveAircrafts();

	@Query("SELECT (COUNT(r) * 1.0) / (SELECT COUNT(a) FROM Review a) FROM Review r WHERE r.score > 5.00")
	Double getRatioHighScoreReviews();

	@Query("SELECT COUNT(r) FROM Review r WHERE r.moment >= :moment")
	Integer countReviewsLast10Weeks(Date moment);

	@Query("SELECT COUNT(r) FROM Review r WHERE r.moment = :date")
	Integer countReviewsPerDay(Date date);
}
