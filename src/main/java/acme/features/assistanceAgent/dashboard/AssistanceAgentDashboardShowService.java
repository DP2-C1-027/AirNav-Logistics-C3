
package acme.features.assistanceAgent.dashboard;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.LegStatistics;
import acme.forms.AssistanceAgentDashboard;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentDashboardShowService extends AbstractGuiService<AssistanceAgent, AssistanceAgentDashboard> {

	@Autowired
	private AssistanceAgentDashboardRepository repository;


	@Override
	public void authorise() {
		boolean status;
		AssistanceAgent agent;

		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		Principal principal = super.getRequest().getPrincipal();
		status = principal.hasRealm(agent);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AssistanceAgentDashboard dashboard = new AssistanceAgentDashboard();

		AssistanceAgent agent;

		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		int agentId = agent.getId();

		// Ratios de claims
		dashboard.setRatioOfResolvedClaims(this.repository.ratioOfResolvedClaims(agentId));
		dashboard.setRatioOfRejectedClaims(this.repository.ratioOfRejectedClaims(agentId));
		dashboard.setTopThreeMonthsWithMostClaims(this.repository.topThreeMonthsWithMostClaims(agentId));

		LegStatistics statsLogsPerClaim = new LegStatistics();
		statsLogsPerClaim.setAverage(this.repository.averageNumberOfLogsPerClaim(agentId));
		statsLogsPerClaim.setMin(this.repository.minimumNumberOfLogsPerClaim(agentId));
		statsLogsPerClaim.setMax(this.repository.maximumNumberOfLogsPerClaim(agentId));
		statsLogsPerClaim.setDeviation(this.repository.deviationNumberOfLogsPerClaim(agentId));
		dashboard.setStatsLogsPerClaim(statsLogsPerClaim);

		Date moment = MomentHelper.getCurrentMoment();
		LocalDate localDateMoment = moment.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate oneYearAgo = localDateMoment.minusYears(1);
		Date oneYearAgoDate = Date.from(oneYearAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
		// Obtener los conteos mensuales
		List<Object[]> monthlyCounts = this.repository.getMonthlyClaimsCounts(agentId, oneYearAgoDate, moment);

		// Calcular estadísticas
		List<Double> counts = monthlyCounts.stream().map(obj -> ((Number) obj[2]).doubleValue()).collect(Collectors.toList());

		LegStatistics statsClaimsLastYear = new LegStatistics();

		if (!counts.isEmpty()) {
			DoubleSummaryStatistics stats = counts.stream().mapToDouble(Double::doubleValue).summaryStatistics();

			statsClaimsLastYear.setAverage(stats.getAverage());
			statsClaimsLastYear.setMin(stats.getMin());
			statsClaimsLastYear.setMax(stats.getMax());

			// Calcular desviación estándar
			double variance = counts.stream().mapToDouble(count -> Math.pow(count - stats.getAverage(), 2)).average().orElse(0.0);
			statsClaimsLastYear.setDeviation(Math.sqrt(variance));
		} else {
			// Valores por defecto si no hay datos
			statsClaimsLastYear.setAverage(0.0);
			statsClaimsLastYear.setMin(0.0);
			statsClaimsLastYear.setMax(0.0);
			statsClaimsLastYear.setDeviation(0.0);
		}
		dashboard.setStatsClaimsLastMonth(statsClaimsLastYear);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AssistanceAgentDashboard object) {
		String top3months = String.join(" ", object.getTopThreeMonthsWithMostClaims());

		Dataset dataset = super.unbindObject(object, "ratioOfResolvedClaims", "ratioOfRejectedClaims", "statsLogsPerClaim", "statsClaimsLastMonth");
		dataset.put("topThreeMonthsWithMostClaims", top3months);

		super.getResponse().addData(dataset);
	}
}
