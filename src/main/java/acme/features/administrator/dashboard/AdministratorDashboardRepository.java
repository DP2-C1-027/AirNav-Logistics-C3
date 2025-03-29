/*
 * AdministratorDashboardRepository.java
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
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository {

	@Query("SELECT a.operationalScope, COUNT(a) FROM Airport a GROUP BY a.operationalScope")
	List<Object[]> getAmountAirportsGroupedByOperationalScope();

	@Query("SELECT al.type, COUNT(al) FROM Airline al GROUP BY al.type")
	List<Object[]> getAmountAirlineGroupedByType();

	@Query("SELECT (COUNT(al) * 1.0) / (SELECT COUNT(a) FROM Airline a) FROM Airline al WHERE al.email IS NOT NULL AND al.phoneNumber IS NOT NULL")
	Double getRatioAirlinesWithEmailAndPhone();

	@Query("SELECT (COUNT(ac) * 1.0) / (SELECT COUNT(a) FROM Aircraft a) FROM Aircraft ac WHERE ac.status = 'ACTIVE_SERVICE'")
	Double getRatioActiveAircrafts();

	@Query("SELECT (COUNT(r) * 1.0) / (SELECT COUNT(a) FROM Review a) FROM Review r WHERE r.score > 5.00")
	Double getRatioHighScoreReviews();

	@Query("SELECT COUNT(r) FROM Review r WHERE r.moment >= :moment")
	Integer countReviewsLast10Weeks(Date moment);

	@Query("SELECT AVG(r.score) FROM Review r WHERE r.moment >= :moment")
	Double averageReviewScoreLast10Weeks(Date moment);

	@Query("SELECT MIN(r.score) FROM Review r WHERE r.moment >= :moment")
	Double minReviewScoreLast10Weeks(Date moment);

	@Query("SELECT MAX(r.score) FROM Review r WHERE r.moment >= :moment")
	Double maxReviewScoreLast10Weeks(Date moment);

	@Query("SELECT SQRT(SUM((r.score - (SELECT AVG(r2.score) FROM Review r2 WHERE r2.moment >= :moment)) * (r.score - (SELECT AVG(r2.score) FROM Review r2 WHERE r2.moment >= :moment))) / COUNT(r)) FROM Review r WHERE r.moment >= :moment")
	Double deviationReviewScoreLast10Weeks(Date moment);

}
