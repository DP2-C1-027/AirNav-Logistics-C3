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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
import acme.entities.flightAssignment.FlightAssignment;
import acme.forms.FlightCrewMemberDashboard;
import acme.realms.flightcrewmember.FlightCrewMember;

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
		Integer legsWithIncidentSeverity3 = this.repository.countLegsWithSeverity(flightCrewMemberId, 0, 3);
		Integer legsWithIncidentSeverity7 = this.repository.countLegsWithSeverity(flightCrewMemberId, 4, 7);
		Integer legsWithIncidentSeverity10 = this.repository.countLegsWithSeverity(flightCrewMemberId, 8, 10);

		//	The crew members who were assigned with him or her in their last leg.  
		List<FlightAssignment> lastAssignmentList = this.repository.findFlightAssignment(flightCrewMemberId, PageRequest.of(0, 1));
		FlightAssignment lastAssignment = !lastAssignmentList.isEmpty() ? lastAssignmentList.get(0) : null;
		List<String> lastLegMembers = lastAssignment != null ? this.repository.findCrewMembersInLastLeg(lastAssignment.getLeg().getId(), PageRequest.of(0, 5)).stream().map(x -> x.getIdentity().getFullName()).toList()
			: List.of(flightCrewMember.getIdentity().getFullName());

		//	Their flight assignments grouped by their statuses.
		Map<CurrentStatus, Integer> flightAssignmentsGroupedByStatus = new HashMap<>();
		for (CurrentStatus status : CurrentStatus.values())
			flightAssignmentsGroupedByStatus.put(status, this.repository.countFlightAssignmentsByStatus(flightCrewMemberId, status));

		//	Minimum, maximum, average and deviation of flight assignments in the last month
		Statistics flightAssignmentsStatsLastMonth = new Statistics();
		Date currentDate = MomentHelper.getCurrentMoment();
		Instant instant = currentDate.toInstant();
		ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
		int lastYear = zonedDateTime.getYear() - 1;

		// Count of flight assignments last year
		Integer count = this.repository.countFlightAssignmentsLastYear(lastYear, flightCrewMemberId);

		// Average of flight assignments per month
		double average = (double) count / 12;

		// Query each month to search the amount of flight assignments
		Integer countPerMonth = 0;
		List<Integer> assignmentsPerMonth = new ArrayList<>();
		for (int month = 1; month <= 12; month++) {
			countPerMonth = this.repository.countFlightAssignmentsPerMonthAndYear(flightCrewMemberId, lastYear, month);
			assignmentsPerMonth.add(countPerMonth != null ? countPerMonth : 0);
		}

		Integer min = assignmentsPerMonth.stream().min(Integer::compareTo).orElse(0);
		Integer max = assignmentsPerMonth.stream().max(Integer::compareTo).orElse(0);
		double standardDeviation = Math.sqrt(assignmentsPerMonth.stream().mapToDouble(n -> Math.pow(n - average, 2)).average().orElse(0.0));

		flightAssignmentsStatsLastMonth.setCount(count);
		flightAssignmentsStatsLastMonth.setAverage(average);
		flightAssignmentsStatsLastMonth.setMin(min.doubleValue());
		flightAssignmentsStatsLastMonth.setMax(max.doubleValue());
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
