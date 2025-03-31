
package acme.features.assistanceAgent.dashboard;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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
		Date moment = MomentHelper.getCurrentMoment();
		LocalDate localDateMoment = moment.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate oneMonthAgo = localDateMoment.minusMonths(1);
		Date oneMonthAgoDate = Date.from(oneMonthAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
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

		// Estadísticas de claims del último mes
		LegStatistics statsClaimsLastMonth = new LegStatistics();
		statsClaimsLastMonth.setAverage(this.repository.averageResolutionOfClaimsLastMonth(agentId, oneMonthAgoDate));
		statsClaimsLastMonth.setMin(this.repository.minimumResolutionOfClaimsLastMonth(agentId, oneMonthAgoDate));
		statsClaimsLastMonth.setMax(100.0);
		statsClaimsLastMonth.setDeviation(this.repository.deviationResolutionOfClaimsLastMonth(agentId, oneMonthAgoDate));
		dashboard.setStatsClaimsLastMonth(statsClaimsLastMonth);

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
