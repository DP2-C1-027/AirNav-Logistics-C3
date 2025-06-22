/*
 * AssistanceAgentTrackingLogUpdateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.assistanceAgent.trackingLogs;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.Indicator;
import acme.entities.claims.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interfaced ------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		TrackingLog trackingLog;
		AssistanceAgent assistance;
		Claim claim;
		if (super.getRequest().hasData("id")) {
			Integer trackingLogId;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				trackingLogId = Integer.valueOf(isInteger);
			else
				trackingLogId = Integer.valueOf(-1);

			trackingLog = !trackingLogId.equals(Integer.valueOf(-1)) ? this.repository.findOneTrackingLogById(trackingLogId) : null;
			claim = trackingLog != null ? this.repository.findClaimByTrackingLogId(trackingLogId) : null;
			assistance = claim != null ? claim.getRegisteredBy() : null;
			status = assistance == null ? false : trackingLog.isDraftMode() && super.getRequest().getPrincipal().hasRealm(assistance);

		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog TrackingLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		TrackingLog = this.repository.findOneTrackingLogById(id);

		super.getBuffer().addData(TrackingLog);
	}

	@Override
	public void bind(final TrackingLog TrackingLog) {
		super.bindObject(TrackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "indicator", "claim");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		if (trackingLog.getIndicator() != null && (trackingLog.getIndicator() == Indicator.ACCEPTED || trackingLog.getIndicator() == Indicator.REJECTED)) {

			super.state(trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() == 100, "resolutionPercentage", "assistance-agent.tracking-log.form.error.percentage-not-100");

			super.state(trackingLog.getResolutionDetails() != null && !trackingLog.getResolutionDetails().isEmpty(), "resolutionDetails", "assistance-agent.tracking-log.form.error.resolution-required");
		}

		if (!trackingLog.getResolutionDetails().isEmpty())
			super.state(trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() == 100, "resolutionDetails", "assistance-agent.tracking-log.form.error.resolutionDetails-not-admited");

		//  porcentaje debe ser >= al último TrackingLog
		if (trackingLog.getClaim() != null && trackingLog.getClaim().getId() != 0 && !super.getBuffer().getErrors().hasErrors("resolutionPercentage")) {

			// Obtener el último TrackingLog ordenado por fecha descendente
			List<TrackingLog> previousLogs = this.repository.findTrackingLogsByClaimIdOrderedByCreationDate(trackingLog.getClaim().getId());

			if (!previousLogs.isEmpty()) {
				TrackingLog lastLog = previousLogs.get(0);

				super.state(trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() >= lastLog.getResolutionPercentage(), "resolutionPercentage", "assistance-agent.tracking-log.form.error.percentage-not-increasing");
			}
		}
	}

	@Override
	public void perform(final TrackingLog TrackingLog) {
		TrackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		this.repository.save(TrackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		Collection<Claim> claims;
		AssistanceAgent assistance = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		claims = this.repository.findAllClaimsByAgent(assistance.getId());
		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "draftMode", "resolutionPercentage", "indicator");

		SelectChoices claimsChoices = SelectChoices.from(claims, "passengerEmail", trackingLog.getClaim());
		dataset.put("claim", claimsChoices);

		SelectChoices statusChoices = SelectChoices.from(Indicator.class, trackingLog.getIndicator());
		dataset.put("indicator", statusChoices);

		super.getResponse().addData(dataset);
	}

}
