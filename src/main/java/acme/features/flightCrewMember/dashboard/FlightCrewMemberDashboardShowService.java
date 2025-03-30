/*
 * FlightCrewMemberDashboardShowService.java
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

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Statistics;
import acme.entities.flightAssignment.CurrentStatus;
import acme.forms.FlightCrewMemberDashboard;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberDashboardShowService extends AbstractGuiService<FlightCrewMember, FlightCrewMemberDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightCrewMemberDashboard dashboard = new FlightCrewMemberDashboard();
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		int flightCrewMemberId = flightCrewMember.getId();

		//	The last five destinations to which they have been assigned. 
		List<String> lastFiveDestinations = this.repository.findLastFiveDestinations(flightCrewMemberId, PageRequest.of(0, 5));

		//	The number of legs that have an activity log record with an incident severity rang-ing from 0 up to 3, 4 up to 7, and 8 up to 10
		Integer legsWithIncidentSeverity3 = this.repository.countLegsWithSeverity(0, 3);
		Integer legsWithIncidentSeverity7 = this.repository.countLegsWithSeverity(4, 7);
		Integer legsWithIncidentSeverity10 = this.repository.countLegsWithSeverity(8, 10);

		//	The crew members who were assigned with him or her in their last leg.  
		List<String> lastLegCrewMembers = this.repository.findCrewMembersInLastLeg(flightCrewMemberId);

		//	Their flight assignments grouped by their statuses.
		List<Object[]> flightAssigmentsResult = this.repository.countFlightAssignmentsGroupedByStatus(flightCrewMemberId);

		Map<CurrentStatus, Integer> flightAssignmentsGroupedByStatus = new HashMap<>();

		for (Object[] result : flightAssigmentsResult) {
			CurrentStatus statusType = (CurrentStatus) result[0];
			Integer count = ((Long) result[1]).intValue();
			flightAssignmentsGroupedByStatus.put(statusType, count);
		}

		//	Minimum, maximum, average and deviation of flight assignments in the last month
		Statistics flightAssignmentsStatsLastMonth = new Statistics();
		Date dateMinus30Days = MomentHelper.deltaFromCurrentMoment(-30, ChronoUnit.DAYS);

		Integer count = this.repository.countFlightAssignmentsLastMonth(dateMinus30Days, flightCrewMemberId);
		//		Double average = this.repository.averageFlightAssignmentsLastMonth(dateMinus30Days, flightCrewMemberId);
		//		Double min = this.repository.minFlightAssignmentsLastMonth(dateMinus30Days, flightCrewMemberId);
		//		Double max = this.repository.maxFlightAssignmentsLastMonth(dateMinus30Days, flightCrewMemberId);
		//		Double deviation = this.repository.deviationFlightAssignmentsLastMonth(dateMinus30Days, flightCrewMemberId, average);

		flightAssignmentsStatsLastMonth.setCount(count);
		//		flightAssignmentsStatsLastMonth.setAverage(average);
		//		flightAssignmentsStatsLastMonth.setMin(min);
		//		flightAssignmentsStatsLastMonth.setMax(max);
		//		flightAssignmentsStatsLastMonth.setDeviation(deviation);

		dashboard.setLastFiveDestinations(lastFiveDestinations);
		dashboard.setLegsWithIncidentSeverity3(legsWithIncidentSeverity3);
		dashboard.setLegsWithIncidentSeverity7(legsWithIncidentSeverity7);
		dashboard.setLegsWithIncidentSeverity10(legsWithIncidentSeverity10);
		dashboard.setLastLegCrewMembers(lastLegCrewMembers);
		dashboard.setFlightAssignmentsGroupedByStatus(flightAssignmentsGroupedByStatus);
		dashboard.setFlightAssignmentsStatsLastMonth(flightAssignmentsStatsLastMonth);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final FlightCrewMemberDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, //
			"lastFiveDestinations", "legsWithIncidentSeverity3", // 
			"legsWithIncidentSeverity7", "legsWithIncidentSeverity10", //
			"lastLegCrewMembers", "flightAssignmentsGroupedByStatus", "flightAssignmentsStatsLastMonth");

		super.getResponse().addData(dataset);
	}

}
