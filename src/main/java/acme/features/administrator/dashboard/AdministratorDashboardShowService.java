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
		AdministratorDashboard dashboard;
		// Obtener los resultados de las consultas
		List<Object[]> airportsResult = this.repository.getAmountAirportsGroupedByOperationalScope();
		List<Object[]> airlinesResult = this.repository.getAmountAirlineGroupedByType();

		// Crear Mapas para almacenar los resultados procesados
		Map<OperationalScope, Integer> amountAirportsGroupedByOperationalScope = new HashMap<>();
		Map<AirlineType, Integer> amountAirlineGroupedByType = new HashMap<>();

		// Procesar los resultados de los aeropuertos
		for (Object[] result : airportsResult) {
			OperationalScope operationalScope = (OperationalScope) result[0];
			Integer count = ((Long) result[1]).intValue();
			amountAirportsGroupedByOperationalScope.put(operationalScope, count);
		}

		// Procesar los resultados de las aerolíneas
		for (Object[] result : airlinesResult) {
			AirlineType airlineType = (AirlineType) result[0];
			Integer count = ((Long) result[1]).intValue();
			amountAirlineGroupedByType.put(airlineType, count);
		}

		Double ratioAirlinesEmailAndPhone = this.repository.getRatioAirlinesWithEmailAndPhone();
		Double ratioActiveAircrafts = this.repository.getRatioActiveAircrafts();
		Double ratioHighScoreReviews = this.repository.getRatioHighScoreReviews();

		Statistics adminReviews = new Statistics();
		Date dateMinus70Days = MomentHelper.deltaFromCurrentMoment(-70, ChronoUnit.DAYS);
		Integer count = this.repository.countReviewsLast10Weeks(dateMinus70Days);
		Double average = this.repository.averageReviewScoreLast10Weeks(dateMinus70Days);
		Double min = this.repository.minReviewScoreLast10Weeks(dateMinus70Days);
		Double max = this.repository.maxReviewScoreLast10Weeks(dateMinus70Days);
		Double deviation = this.repository.deviationReviewScoreLast10Weeks(dateMinus70Days);

		adminReviews.setCount(count);
		adminReviews.setAverage(average);
		adminReviews.setMin(min);
		adminReviews.setMax(max);
		adminReviews.setDeviation(deviation);

		dashboard = new AdministratorDashboard();
		dashboard.setAmountAirportsGroupedByOperationalScope(amountAirportsGroupedByOperationalScope);
		dashboard.setAmountAirlineGroupedByType(amountAirlineGroupedByType);
		dashboard.setRatioAirlinesEmailAndPhone(ratioAirlinesEmailAndPhone);
		dashboard.setRatioActiveAircrafts(ratioActiveAircrafts);
		dashboard.setRatioHighScoreReviews(ratioHighScoreReviews);
		dashboard.setAdminReviews(adminReviews);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AdministratorDashboard administratorDashboard) {
		Dataset dataset = super.unbindObject(administratorDashboard, //
			"amountAirportsGroupedByOperationalScope", "amountAirlineGroupedByType", // 
			"ratioAirlinesEmailAndPhone", "ratioActiveAircrafts", //
			"ratioHighScoreReviews", "adminReviews");

		super.getResponse().addData(dataset);
	}

}
