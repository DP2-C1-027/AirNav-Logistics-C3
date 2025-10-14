/*
 * AssistanceAgentTrackingLogCreateService.java
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
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		AssistanceAgent assistance = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		Claim claim;

		status = super.getRequest().getPrincipal().hasRealm(assistance);

		if (super.getRequest().hasData("id")) {

			Integer trackingLogId;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				trackingLogId = Integer.valueOf(isInteger);
			else
				trackingLogId = Integer.valueOf(-1);

			status = super.getRequest().getPrincipal().hasRealm(assistance);
			if (!trackingLogId.equals(Integer.valueOf(0)))
				status = false;
		}
		// aqui hay algo mal que me peta la peticion

		if (super.getRequest().hasData("claimId")) {
			Integer claimId;
			String isIntegerClaim;
			isIntegerClaim = super.getRequest().getData("claimId", String.class).trim();
			if (!isIntegerClaim.isBlank() && isIntegerClaim.chars().allMatch((e) -> e > 47 && e < 58))
				claimId = Integer.valueOf(isIntegerClaim);
			else
				claimId = Integer.valueOf(-1);

			if (claimId.equals(Integer.valueOf(-1)))
				status = false;

			if (!claimId.equals(Integer.valueOf(-1))) {
				claim = claimId != null ? this.repository.findOneClaimById(claimId) : null;
				if (claim == null)
					status = false;
			}

			Claim claim2 = status ? this.repository.findOneClaimById(claimId) : null;
			if (claim2 == null || !claim2.getRegisteredBy().equals(assistance))
				status = false;

			List<TrackingLog> previousLogs = !claimId.equals(Integer.valueOf(-1)) ? this.repository.findTrackingLogsByClaimIdOrderedByPercentaje(claimId) : null;
			if (previousLogs != null && !previousLogs.isEmpty()) {
				TrackingLog lastLog = previousLogs.get(0);
				if (!(lastLog.isDraftMode() || lastLog.getResolutionPercentage() != null && lastLog.getResolutionPercentage() == 100 && lastLog.getIndicator() == Indicator.ACCEPTED))
					status = false;
			}

		}

		else if (super.getRequest().getMethod().equals("POST"))
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog TrackingLog;
		TrackingLog = new TrackingLog();

		TrackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		TrackingLog.setCreationMoment(MomentHelper.getCurrentMoment());

		TrackingLog.setDraftMode(true);
		int claimId = super.getRequest().getData("claimId", int.class);

		Claim claim = this.repository.findOneClaimById(claimId);
		TrackingLog.setClaim(claim);

		super.getBuffer().addData(TrackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "stepUndergoing", "resolutionPercentage", "resolutionDetails", "indicator");

	}

	@Override
	public void validate(final TrackingLog trackingLog) {

		// Validación de claim no nulo (seguridad crítica)
		super.state(trackingLog.getClaim() != null, "claim", "assistance-agent.tracking-log.form.error.claim-required");

		super.state(trackingLog.getClaim() instanceof Claim, "claim", "assistance-agent.tracking-log.form.error.claim-required");

		// Validación de ownership del claim
		if (trackingLog.getClaim() != null) {
			AssistanceAgent agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
			super.state(trackingLog.getClaim().getRegisteredBy().equals(agent), "claim", "assistance-agent.tracking-log.form.error.claim-ownership");
		}

		// Validaciones existentes para indicator...
		if (trackingLog.getIndicator() != null && (trackingLog.getIndicator() == Indicator.ACCEPTED || trackingLog.getIndicator() == Indicator.REJECTED)) {
			super.state(trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() == 100, "resolutionPercentage", "assistance-agent.tracking-log.form.error.percentage-not-100");
			super.state(trackingLog.getResolutionDetails() != null && !trackingLog.getResolutionDetails().isEmpty(), "resolutionDetails", "assistance-agent.tracking-log.form.error.resolution-required");
		}
		if (!trackingLog.getResolutionDetails().isEmpty() && trackingLog.getResolutionDetails() != null)
			super.state(trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() == 100, "resolutionDetails", "assistance-agent.tracking-log.form.error.resolutionDetails-not-admited");

		// Validación de porcentaje incremental
		if (!trackingLog.getResolutionDetails().isEmpty() && trackingLog.getClaim() != null && trackingLog.getClaim().getId() != 0 && !super.getBuffer().getErrors().hasErrors("resolutionPercentage")) {
			List<TrackingLog> previousLogs = this.repository.findTrackingLogsByClaimIdOrderedByCreationDate(trackingLog.getClaim().getId());

			if (!previousLogs.isEmpty()) {
				TrackingLog lastLog = previousLogs.get(0);
				super.state(trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() >= lastLog.getResolutionPercentage(), "resolutionPercentage", "assistance-agent.tracking-log.form.error.percentage-not-increasing");
			}
		}

		// Validación para porcentaje 100% después de publicar
		int claimId = super.getRequest().getData("claimId", int.class);
		List<TrackingLog> previousLogs = this.repository.findTrackingLogsByClaimIdOrderedByCreationDate(claimId);

		if (!previousLogs.isEmpty() && !trackingLog.getResolutionDetails().isEmpty()) {
			TrackingLog lastLog = previousLogs.get(0);
			if (!lastLog.isDraftMode())
				super.state(trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() == 100, "resolutionPercentage", "assistance-agent.tracking-log.form.error.after-publish-percentage-mustbe-100");
			super.state(trackingLog.getIndicator() != null && trackingLog.getIndicator() == Indicator.REJECTED, "indicator", "assistance-agent.tracking-log.form.error.after-publish-mustbe-rejected");

		}
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "creationMoment", "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "resolutionDetails", "indicator", "claim");

		if (trackingLog.getClaim() != null)
			dataset.put("claimId", trackingLog.getClaim().getId());
		else
			dataset.put("claimId", 0);

		SelectChoices statusChoices = SelectChoices.from(Indicator.class, trackingLog.getIndicator());
		dataset.put("indicator", statusChoices);

		super.getResponse().addData(dataset);
	}

}
