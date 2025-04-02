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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Statistics;
import acme.entities.flightAssignment.CurrentStatus;
import acme.entities.flightAssignment.FlightAssignment;
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
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(status);
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
		List<FlightAssignment> assigmentsRelated = this.repository.findFlightAssignment(flightCrewMemberId);
		List<String> lastLegMembers = new ArrayList<>();
		if (!assigmentsRelated.isEmpty()) {
			int legId = assigmentsRelated.get(0).getLeg().getId();
			List<FlightCrewMember> flightCrewMembers = this.repository.findCrewMembersInLastLeg(legId);
			lastLegMembers = flightCrewMembers.stream().map(x -> x.getIdentity().getFullName()).toList();
		}

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
		Date dateMinus1Year = MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS);

		Integer count = this.repository.countFlightAssignmentsLastYear(MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS), flightCrewMemberId);
		Double average = (double) count / 12;

		// Year method is deprecated in Date type, solution:
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateMinus1Year);
		int year = calendar.get(Calendar.YEAR);

		// Query each month to search the amount of flight assignments
		Integer countPerMonth = 0;
		List<Integer> assignmentsPerMonth = new ArrayList<>();
		for (int month = 1; month <= 12; month++) {
			countPerMonth = this.repository.countFlightAssignmentsPerMonthAndYear(flightCrewMemberId, year, month);
			assignmentsPerMonth.add(countPerMonth != null ? countPerMonth : 0);
		}

		Optional<Integer> min = assignmentsPerMonth.stream().min(Integer::compareTo);
		Optional<Integer> max = assignmentsPerMonth.stream().max(Integer::compareTo);
		double standardDeviation = Math.sqrt(assignmentsPerMonth.stream().mapToDouble(n -> Math.pow(n - average, 2)).average().orElse(0.0));

		flightAssignmentsStatsLastMonth.setCount(count);
		flightAssignmentsStatsLastMonth.setAverage(average);
		flightAssignmentsStatsLastMonth.setMin(min.orElse(0).doubleValue());
		flightAssignmentsStatsLastMonth.setMax(max.orElse(0).doubleValue());
		flightAssignmentsStatsLastMonth.setDeviation(standardDeviation);

		dashboard.setLastFiveDestinations(lastFiveDestinations);
		dashboard.setLegsWithIncidentSeverity3(legsWithIncidentSeverity3);
		dashboard.setLegsWithIncidentSeverity7(legsWithIncidentSeverity7);
		dashboard.setLegsWithIncidentSeverity10(legsWithIncidentSeverity10);
		dashboard.setLastLegCrewMembers(lastLegMembers);
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
