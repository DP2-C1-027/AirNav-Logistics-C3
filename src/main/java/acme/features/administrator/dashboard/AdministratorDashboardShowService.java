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

package acme.features.administrator.dashboard;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Statistics;
import acme.entities.airline.AirlineType;
import acme.entities.airport.OperationalScope;
import acme.forms.AdministratorDashboard;

@GuiService
public class AdministratorDashboardShowService extends AbstractGuiService<Administrator, AdministratorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AdministratorDashboard dashboard = new AdministratorDashboard();

		// Amount of airports grouped by operational scope
		Map<OperationalScope, Integer> airportsGroupedByOperationalScope = new HashMap<>();
		for (OperationalScope op : OperationalScope.values())
			airportsGroupedByOperationalScope.put(op, this.repository.countAirportsByOperationalScope(op));

		// Amount of airlines grouped by type
		Map<AirlineType, Integer> airlinesGroupedByAirlineType = new HashMap<>();
		for (AirlineType type : AirlineType.values())
			airlinesGroupedByAirlineType.put(type, this.repository.countAirlinesByType(type));

		// Ratio of all airlines with both an email and phone number
		Double ratioAirlinesEmailAndPhone = this.repository.getRatioAirlinesWithEmailAndPhone();

		// Ratio of active and nonActive aircrafts
		Double ratioActiveAircrafts = this.repository.getRatioActiveAircrafts();
		Double ratioInactiveAircrafts = this.repository.getRatioInactiveAircrafts();

		// Ratio of all reviews with a score above 5.00
		Double ratioHighScoreReviews = this.repository.getRatioHighScoreReviews();

		// Count, Average, Minimum, Maximum, and deviation of the number of reviews posted over the last 10 weeks
		Statistics adminReviews = new Statistics();
		Date dateMinus70Days = MomentHelper.deltaFromCurrentMoment(-70, ChronoUnit.DAYS);
		Integer count = this.repository.countReviewsLast10Weeks(dateMinus70Days);
		Double average = (double) count / 70;

		Integer countPerDay = 0;
		List<Integer> reviewsPerDay = new ArrayList<>();
		for (int day = 69; day >= 0; day--) {
			Date dayDate = MomentHelper.deltaFromCurrentMoment(-day, ChronoUnit.DAYS);
			countPerDay = this.repository.countReviewsPerDay(dayDate);
			reviewsPerDay.add(countPerDay != null ? countPerDay : 0);
		}

		Integer min = reviewsPerDay.stream().min(Integer::compareTo).orElse(0);
		Integer max = reviewsPerDay.stream().max(Integer::compareTo).orElse(0);
		double deviation = Math.sqrt(reviewsPerDay.stream().mapToDouble(n -> Math.pow(n - average, 2)).average().orElse(0.0));

		adminReviews.setCount(count);
		adminReviews.setAverage(average);
		adminReviews.setMin(min.doubleValue());
		adminReviews.setMax(max.doubleValue());
		adminReviews.setDeviation(deviation);

		dashboard.setAmountAirportsGroupedByOperationalScope(airportsGroupedByOperationalScope);
		dashboard.setAmountAirlineGroupedByType(airlinesGroupedByAirlineType);
		dashboard.setRatioAirlinesEmailAndPhone(ratioAirlinesEmailAndPhone);
		dashboard.setRatioActiveAircrafts(ratioActiveAircrafts);
		dashboard.setRatioInactiveAircrafts(ratioInactiveAircrafts);
		dashboard.setRatioHighScoreReviews(ratioHighScoreReviews);
		dashboard.setAdminReviews(adminReviews);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AdministratorDashboard administratorDashboard) {
		Dataset dataset = super.unbindObject(administratorDashboard, //
			"amountAirportsGroupedByOperationalScope", "amountAirlineGroupedByType", // 
			"ratioAirlinesEmailAndPhone", "ratioActiveAircrafts", "ratioInactiveAircrafts",//
			"ratioHighScoreReviews", "adminReviews");

		super.getResponse().addData(dataset);
	}

}
