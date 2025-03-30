
package acme.features.flightCrewMember.visaRequirements;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.visaRequirements.VisaRequirements;

@Repository
public interface FlightCrewMemberVisaRequirementsRepository extends AbstractRepository {

	@Query("SELECT vs FROM VisaRequirements vs WHERE vs.country = :country")
	Collection<VisaRequirements> findAllVisaRequirementsByCountry(String country);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT vs FROM VisaRequirements vs WHERE vs.id = :id")
	VisaRequirements findVisaRequirementsById(int id);

}
