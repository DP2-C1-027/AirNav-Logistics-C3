
package acme.features.assistanceAgent.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AssistanceAgentDashboardRepository extends AbstractRepository {

	@Query("SELECT COUNT(c) FROM Claim c WHERE c.registeredBy.id = :agentId AND EXISTS (SELECT t FROM TrackingLog t WHERE t.claim = c AND t.indicator = 'ACCEPTED')")
	int countResolvedClaims(@Param("agentId") int agentId);

	@Query("SELECT COUNT(c) FROM Claim c WHERE c.registeredBy.id = :agentId AND EXISTS (SELECT t FROM TrackingLog t WHERE t.claim = c AND t.indicator = 'REJECTED')")
	int countRejectedClaims(@Param("agentId") int agentId);

	@Query("SELECT COUNT(c) FROM Claim c WHERE c.registeredBy.id = :agentId")
	int countTotalClaims(@Param("agentId") int agentId);

	default Double ratioOfResolvedClaims(final int agentId) {
		int total = this.countTotalClaims(agentId);
		return total > 0 ? (double) this.countResolvedClaims(agentId) / total : 0.0;
	}

	default Double ratioOfRejectedClaims(final int agentId) {
		int total = this.countTotalClaims(agentId);
		return total > 0 ? (double) this.countRejectedClaims(agentId) / total : 0.0;
	}

	@Query("SELECT FUNCTION('MONTHNAME', c.registrationMoment) as month, COUNT(c) as count " + "FROM Claim c WHERE c.registeredBy.id = :agentId " + "GROUP BY FUNCTION('MONTH', c.registrationMoment), month " + "ORDER BY COUNT(c) DESC")  // Usar COUNT(c) en lugar del alias
	List<Object[]> findMonthsWithMostClaims(@Param("agentId") int agentId);

	default List<String> topThreeMonthsWithMostClaims(final int agentId) {
		return this.findMonthsWithMostClaims(agentId).stream().limit(3).map(arr -> (String) arr[0]).toList();
	}

	@Query("SELECT AVG((SELECT COUNT(t) FROM TrackingLog t WHERE t.claim = c)) " + "FROM Claim c WHERE c.registeredBy.id = :agentId")
	Double averageNumberOfLogsPerClaim(@Param("agentId") int agentId);

	@Query("SELECT MIN((SELECT COUNT(t) FROM TrackingLog t WHERE t.claim = c)) " + "FROM Claim c WHERE c.registeredBy.id = :agentId")
	Double minimumNumberOfLogsPerClaim(@Param("agentId") int agentId);

	@Query("SELECT MAX((SELECT COUNT(t) FROM TrackingLog t WHERE t.claim = c)) " + "FROM Claim c WHERE c.registeredBy.id = :agentId")
	Double maximumNumberOfLogsPerClaim(@Param("agentId") int agentId);

	@Query("SELECT STDDEV((SELECT COUNT(t) FROM TrackingLog t WHERE t.claim = c)) " + "FROM Claim c WHERE c.registeredBy.id = :agentId")
	Double deviationNumberOfLogsPerClaim(@Param("agentId") int agentId);

	// Como Claim no tiene amount, usaremos resolutionPercentage de sus TrackingLogs
	@Query("SELECT COUNT(c) FROM Claim c WHERE c.registeredBy.id = :agentId AND c.registrationMoment >= :date")
	Integer countClaimsLastMonth(@Param("agentId") int agentId, @Param("date") Date date);

	@Query("SELECT AVG((SELECT AVG(t.resolutionPercentage) FROM TrackingLog t WHERE t.claim = c)) " + "FROM Claim c WHERE c.registeredBy.id = :agentId AND c.registrationMoment >= :date")
	Double averageResolutionOfClaimsLastMonth(@Param("agentId") int agentId, @Param("date") Date date);

	@Query("SELECT MIN((SELECT MIN(t.resolutionPercentage) FROM TrackingLog t WHERE t.claim = c)) " + "FROM Claim c WHERE c.registeredBy.id = :agentId AND c.registrationMoment >= :date")
	Double minimumResolutionOfClaimsLastMonth(@Param("agentId") int agentId, @Param("date") Date date);

	@Query("SELECT MAX((SELECT MAX(t.resolutionPercentage) FROM TrackingLog t WHERE t.claim = c)) " + "FROM Claim c WHERE c.registeredBy.id = :agentId AND c.registrationMoment >= :date")
	Double maximumResolutionOfClaimsLastMonth(@Param("agentId") int agentId, @Param("date") Date date);

	@Query("SELECT STDDEV((SELECT AVG(t.resolutionPercentage) FROM TrackingLog t WHERE t.claim = c)) " + "FROM Claim c WHERE c.registeredBy.id = :agentId AND c.registrationMoment >= :date")
	Double deviationResolutionOfClaimsLastMonth(@Param("agentId") int agentId, @Param("date") Date date);

}
